package com.tokopedia.loginphone.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.otp.common.network.ErrorMessageException;
import com.tokopedia.otp.common.network.OtpErrorCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 4/30/18.
 */
public class TokoCashErrorResponse extends BaseResponseError {

    @SerializedName("errors")
    @Expose
    private List<String> messageError = new ArrayList<>();

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
        return new MessageErrorException(messageError.get(0), getErrorKey());
    }
}
