package com.tokopedia.core.network.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;

import java.io.IOException;
import java.util.List;

/**
 * @author sebastianuskh on 5/2/17.
 */

public class TkpdV4ResponseError extends BaseResponseError{
    private static final String ERROR_KEY = "message_error";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<String> errors = null;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return (errors!= null && errors.size() > 0);
    }

    @Override
    public IOException createException() {
        return new ResponseV4ErrorException(errors);
    }
}
