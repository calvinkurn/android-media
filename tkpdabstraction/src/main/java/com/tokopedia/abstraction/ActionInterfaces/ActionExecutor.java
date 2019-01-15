package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionExecutor<T,V,W,X,Y> {
    void doAction(int actionId, T dataObj, ActionCreator<V,W> actionCreator, ActionUIDelegate<X,Y> actionUIDelegate);
}
