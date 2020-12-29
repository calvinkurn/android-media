package com.tokopedia.notifications.inApp.ruleEngine;

import android.app.Application;

import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataProvider;
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.InterfaceRuleInterpreter;
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;
import com.tokopedia.notifications.inApp.ruleEngine.rulesinterpreter.RuleInterpreterImpl;
import com.tokopedia.notifications.inApp.ruleEngine.storage.DataConsumerImpl;

public class RulesManager {

    private InterfaceRuleInterpreter ruleInterpreter;
    private static RulesManager rulesManager;
    private DataConsumer dataConsumer;


    private RulesManager(Application application, InterfaceRuleInterpreter ruleInterpreter, DataConsumer dataConsumer) {
        this.ruleInterpreter = ruleInterpreter;
        this.dataConsumer = dataConsumer;
        RepositoryManager.initRepository(application);
    }

    public static void initRuleEngine(Application application, InterfaceRuleInterpreter ruleInterpreter, DataConsumer dataConsumer) {
        if (rulesManager == null) {
            synchronized (RulesManager.class) {
                rulesManager = new RulesManager(application, ruleInterpreter, dataConsumer);
            }
        }
    }

    public static RulesManager getInstance() {
        return rulesManager;
    }

    public void checkValidity(String entity, long currentTime, DataProvider dataProvider, int entityHashCode, boolean isActivity) {
        ruleInterpreter.checkForValidity(entity, currentTime, dataProvider, entityHashCode, isActivity);
    }

    public void dataConsumed(long id) {
        dataConsumer.dataShown(id);
    }

    public void dataInflateError(long id) {
        dataConsumer.inflationError(id);
    }

    public void viewDismissed(long id) {
        dataConsumer.viewDismissed(id);
    }

    public void interactedWithView(long id){
        dataConsumer.interactedWithView(id);
    }
}
