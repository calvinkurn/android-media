package com.tokopedia.flight.common.data.model;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;

import java.util.List;

/**
 * Created by User on 11/28/2017.
 */

public class FlightException extends MessageErrorException {
    private List<FlightError> errorList;

    public List<FlightError> getErrorList() {
        return errorList;
    }

    public FlightException(String message, List<FlightError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
