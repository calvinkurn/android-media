package com.tokopedia.analytics.debugger;

public interface IrisLoggerInterface {
    void putSendIrisEvent(String data);

    void putSaveIrisEvent(String data);

    void openSaveActivity();
    void openSendActivity();

}
