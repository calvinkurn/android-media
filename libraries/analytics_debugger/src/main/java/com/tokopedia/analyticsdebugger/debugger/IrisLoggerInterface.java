package com.tokopedia.analyticsdebugger.debugger;

public interface IrisLoggerInterface {
    void putSendIrisEvent(String data, int rowCount);

    void putSaveIrisEvent(String data);

    void openSaveActivity();
    void openSendActivity();

}
