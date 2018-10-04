package com.tokopedia.otp.common.network;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.exception.Header;

import java.io.IOException;

/**
 * @author by nisie on 4/26/18.
 */

public class OtpErrorResponse extends BaseResponseError {

    @SerializedName("header")
    @Expose
    private Header header;

    @Override
    public String getErrorKey() {
        return header.getErrorCode();
    }

    @Override
    public boolean hasBody() {
        return (header != null
                && header.getMessages() != null
                && header.getMessages().size() > 0
                && !TextUtils.isEmpty(header.getErrorCode()));
    }

    @Override
    public IOException createException() {
        return new ErrorMessageException(header.getMessages().get(0), getErrorKey());
    }

}
