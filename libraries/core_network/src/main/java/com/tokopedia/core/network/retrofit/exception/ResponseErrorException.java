package com.tokopedia.core.network.retrofit.exception;

import com.tokopedia.core.network.retrofit.response.Error;

import java.io.IOException;
import java.util.List;

/**
 * @author normansyahputa on 2/13/17.
 */

@Deprecated
public class ResponseErrorException extends IOException {
    private List<Error> errorList;
    public ResponseErrorException(List<Error> errorList) {
        this.errorList = errorList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }
}
