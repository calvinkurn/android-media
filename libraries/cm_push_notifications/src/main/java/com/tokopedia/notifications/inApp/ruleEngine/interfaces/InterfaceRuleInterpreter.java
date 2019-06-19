package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

public interface InterfaceRuleInterpreter {
    void checkForValidity(String entity, int state, long currentTime, DataProvider dataProvider);
}
