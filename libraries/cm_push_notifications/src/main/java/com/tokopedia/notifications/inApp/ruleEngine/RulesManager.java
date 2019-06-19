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
    private long currentTime;


    public RulesManager(Application application){
        ruleInterpreter = new RuleInterpreterImpl();
        dataConsumer = new DataConsumerImpl();
        RepositoryManager.initRepository(application);
    }

    public static void initRuleEngine(Application application){
        if(rulesManager == null) {
            synchronized (RulesManager.class) {
                rulesManager = new RulesManager(application);
            }
        }
    }

    public static RulesManager getInstance(){
        return rulesManager;
    }

    public void checkValidity(String entity, long currentTime, DataProvider dataProvider){
        int state = 0;
        this.currentTime = currentTime;
        ruleInterpreter.checkForValidity(entity, state, currentTime, dataProvider);
    }

    public void dataConsumed(long id){
        dataConsumer.dataShown(id);
    }

    public void dataConsumed(long id, long bufTime){
        dataConsumer.dataShown(id, currentTime+bufTime);
    }

    public void viewDismissed(long id){
        dataConsumer.viewDismissed(id);
    }
}
