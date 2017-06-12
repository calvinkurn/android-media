package com.tokopedia.core.network.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.retrofit.exception.ResponseErrorListStringException;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;

import java.io.IOException;
import java.util.List;

/**
 * @author sebastianuskh on 4/17/17.
 */

public class TkpdV4ResponseError extends BaseResponseError {

    private static final String ERROR_KEY = "message_error";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<String> messageError;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return messageError != null && messageError.size() > 0;
    }

    @Override
    public IOException createException() {
        return new ResponseErrorListStringException(messageError);
    }
}
