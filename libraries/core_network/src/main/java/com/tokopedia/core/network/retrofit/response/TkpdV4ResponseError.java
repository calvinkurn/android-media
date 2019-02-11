package com.tokopedia.core.network.retrofit.response;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;

import java.io.IOException;
import java.util.List;

/**
 * @author sebastianuskh on 5/2/17.
 */

@Deprecated
public class TkpdV4ResponseError extends BaseResponseError {

    private static final String STATUS_OK = "OK";
    private static final String PARAM_MESSAGE_ERROR = "message_error";
    private static final String PARAM_STATUS = "status";

    @SerializedName(PARAM_MESSAGE_ERROR)
    @Expose
    private List<String> messageError;

    @SerializedName(PARAM_STATUS)
    @Expose
    private String status;

    @Override
    public String getErrorKey() {
        return PARAM_MESSAGE_ERROR;
    }

    @Override
    public boolean isResponseErrorValid() {
        if (TextUtils.isEmpty(status)) {
            return false;
        }
        if (status.equalsIgnoreCase(STATUS_OK)) {
            return false;
        }
        return hasBody();
    }

    @Override
    public boolean hasBody() {
        return messageError != null && messageError.size() > 0;
    }

    @Override
    public IOException createException() {
        return new ResponseV4ErrorException(messageError);
    }
}
