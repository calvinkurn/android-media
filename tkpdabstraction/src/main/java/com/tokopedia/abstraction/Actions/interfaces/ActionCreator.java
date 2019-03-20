package com.tokopedia.abstraction.Actions.interfaces;

import java.io.Serializable;

public interface ActionCreator <RESULT_DATA, ERROR_DATA> extends Serializable {
    void actionSuccess(int actionId, RESULT_DATA dataObj);
    void actionError(int actionId, ERROR_DATA dataObj);
}
