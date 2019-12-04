package com.tokopedia.flight.orderlist.network.model;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;

import java.util.List;

/**
 * Created by User on 11/28/2017.
 */

public class FlightOrderException extends MessageErrorException {
    private List<FlightOrderError> errorList;

    public List<FlightOrderError> getErrorList() {
        return errorList;
    }

    public FlightOrderException(String message, List<FlightOrderError> errorList) {
        super(message);
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
