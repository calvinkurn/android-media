package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

public interface DataConsumer {
    void dataShown(long id);
    void viewDismissed(long id);
    void interactedWithView(long id);
    void dataShown(long id, long newSt);
    void inflationError(long id);
}
