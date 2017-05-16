package com.tokopedia.core.network.retrofit.exception;

import java.io.IOException;
import java.util.List;

/**
 * @author sebastianuskh on 5/2/17.
 */

public class ResponseV4ErrorException extends IOException {
    private List<String> errorList;

    public ResponseV4ErrorException(List<String> errorList) {
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
