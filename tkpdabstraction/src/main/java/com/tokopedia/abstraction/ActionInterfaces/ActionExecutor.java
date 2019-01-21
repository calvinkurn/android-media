package com.tokopedia.abstraction.ActionInterfaces;

public interface ActionExecutor<ACTION_DATA,RESULT_DATA, ERROR_DATA,WAIT_DATA, STOPWAIT_DATA> {
    void doAction(int actionId, ACTION_DATA dataObj, ActionCreator<RESULT_DATA, ERROR_DATA> actionCreator, ActionUIDelegate<WAIT_DATA, STOPWAIT_DATA> actionUIDelegate);
}
