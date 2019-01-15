package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionUIDelegate<T,V> {
    void waitForResult(int actionId, T dataObj);
    void stopWaiting(int actionId, V dataObj);
}
