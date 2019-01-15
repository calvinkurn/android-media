package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionCreator <T, V>{
    void actionSuccess(int actionId, T dataObj);
    void actionError(int actionId, V dataObj);
}
