package com.tokopedia.train.common.data.interceptor;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.train.common.data.interceptor.model.TrainNetworkError;

import java.util.List;

public class TrainNetworkException extends MessageErrorException {
    private List<TrainNetworkError> errorList;

    public List<TrainNetworkError> getErrorList() {
        return errorList;
    }

    public TrainNetworkException(String message, List<TrainNetworkError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
