package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

public interface InterfaceRuleInterpreter {
    void checkForValidity(String entity, long currentTime, DataProvider dataProvider);
}
