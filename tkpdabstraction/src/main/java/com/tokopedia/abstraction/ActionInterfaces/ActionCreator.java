package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionCreator <RESULT_DATA, ERROR_DATA>{
    void actionSuccess(int actionId, RESULT_DATA dataObj);
    void actionError(int actionId, ERROR_DATA dataObj);
}
