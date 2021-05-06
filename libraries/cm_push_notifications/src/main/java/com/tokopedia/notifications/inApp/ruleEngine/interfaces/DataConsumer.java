package com.tokopedia.notifications.inApp.ruleEngine.interfaces;

public interface DataConsumer {
    void dataShown(long id);
    void viewDismissed(long id);
    void updateVisibleStateForAlreadyShown();
    void interactedWithView(long id);
    void inflationError(long id);
}
