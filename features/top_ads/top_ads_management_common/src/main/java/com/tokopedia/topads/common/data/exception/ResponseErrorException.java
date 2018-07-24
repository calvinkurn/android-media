package com.tokopedia.topads.common.data.exception;

import com.tokopedia.topads.common.data.response.Error;

import java.io.IOException;
import java.util.List;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class ResponseErrorException extends IOException {
    private List<Error> errorList;
    public ResponseErrorException(List<Error> errorList) {
        this.errorList = errorList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }
}
