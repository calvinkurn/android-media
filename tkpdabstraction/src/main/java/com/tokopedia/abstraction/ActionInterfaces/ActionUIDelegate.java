package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionUIDelegate<WAIT_DATA, STOPWAIT_DATA> {
    void waitForResult(int actionId, WAIT_DATA dataObj);
    void stopWaiting(int actionId, STOPWAIT_DATA dataObj);
}
