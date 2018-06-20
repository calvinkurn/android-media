package com.tokopedia.otp.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;
import java.util.List;

/**
 * @author by nisie on 4/30/18.
 */
public class WSErrorResponse extends BaseResponseError {

    @SerializedName("message_error")
    @Expose
    private List<String> messageError = null;

    @Override
    public String getErrorKey() {
        return String.valueOf(OtpErrorCode.WS_ERROR);
    }

    @Override
    public boolean hasBody() {
        return messageError != null
                && !messageError.isEmpty();
    }

    @Override
    public IOException createException() {
        return new ErrorMessageException(messageError.get(0), getErrorKey());
    }
}
