package com.tokopedia.core.network.retrofit.exception;

import java.io.IOException;
import java.util.List;

/**
 * @author sebastianuskh on 4/17/17.
 */

public class ResponseErrorListStringException extends IOException{
    private List<String> errorList;

    public ResponseErrorListStringException(List<String> errorList) {
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
