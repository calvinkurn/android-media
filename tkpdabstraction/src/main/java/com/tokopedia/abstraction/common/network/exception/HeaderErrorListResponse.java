package com.tokopedia.abstraction.common.network.exception;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;

/**
 * Created by zulfikarrahman on 12/29/17.
 */

public class HeaderErrorListResponse extends BaseResponseError {

    @SerializedName("header")
    @Expose
    private Header header;

    @Override
    public String getErrorKey() {
        return header.getErrorCode();
    }

    @Override
    public boolean hasBody() {
        return (header != null && header.getMessages() != null && header.getMessages().size() > 0);
    }

    @Override
    public IOException createException() {
        return new MessageErrorException(TextUtils.join("\n",header.getMessages()));
    }
}
