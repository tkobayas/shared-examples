package com.sample;

import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class DroolsTest {

    private static final String drlContent = """
            package rules

            rule "Minimal Rule"
            when
                String()
            then
            end
            """;

//    private static final String drlContent = """
//            package rules
//
//            rule "Minimal Rule"
//            when
//                String( this == "x" )
//            then
//            end
//            """;

    @Test
    public void testRules() {

        System.out.println("JDK version: " + System.getProperty("java.version"));

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        kieFileSystem.write("src/main/resources/rules/test.drl", drlContent);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll(); // Crashes here

        KieContainer kcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

        KieBase kbase = kcontainer.getKieBase();

        KieSession ksession = kbase.newKieSession();

        try {
            ksession.insert(new String("XXX"));

            int fired = ksession.fireAllRules();

            System.out.println("Number of Rules executed = " + fired);
        } finally {
            ksession.dispose();
        }
    }
}
