package com.tokopedia.withdraw.domain.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;
import java.util.List;

/**
 * @author by StevenFredian on 08/08/18.
 */

public class WSErrorResponse extends BaseResponseError {

    @SerializedName("message_error")
    @Expose
    private List<String> messageError = null;

    @Override
    public String getErrorKey() {
        return String.valueOf(100);
    }

    @Override
    public boolean hasBody() {
        return messageError != null
                && !messageError.isEmpty();
    }

    @Override
    public IOException createException() {
        String joined = TextUtils.join("\n", messageError);
        return new ErrorMessageException(joined, getErrorKey());
    }


    public class ErrorMessageException extends IOException {

        private String errorCode;

        public ErrorMessageException(String errorMessage) {
            super(errorMessage);
        }

        public ErrorMessageException(String errorMessage, String errorCode) {
            super(errorMessage);
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

}
