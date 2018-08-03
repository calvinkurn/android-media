package com.tokopedia.train.common.data.interceptor;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.train.common.data.interceptor.model.TrainError;
import com.tokopedia.train.common.data.interceptor.model.TrainNetworkError;

import java.util.List;

public class TrainNetworkException extends MessageErrorException {
    private List<TrainError> errorList;

    public List<TrainError> getErrorList() {
        return errorList;
    }

    public TrainNetworkException(String message, List<TrainError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
