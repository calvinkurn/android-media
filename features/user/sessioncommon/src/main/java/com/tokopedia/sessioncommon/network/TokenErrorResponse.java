package com.tokopedia.sessioncommon.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import java.io.IOException;

/**
 * @author by nisie on 10/22/18.
 */
public class TokenErrorResponse extends BaseResponseError {

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("error_description")
    @Expose
    private String errorDescription;

    @Override
    public String getErrorKey() {
        return String.valueOf(ErrorHandlerSession.ErrorCode.WS_ERROR);
    }

    @Override
    public boolean hasBody() {
        return error!= null && !error.isEmpty();
    }

    @Override
    public IOException createException() {
        return new TokenErrorException(error, errorDescription, getErrorKey());
    }
}
