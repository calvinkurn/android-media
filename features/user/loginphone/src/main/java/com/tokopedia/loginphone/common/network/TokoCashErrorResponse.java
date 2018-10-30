package com.tokopedia.loginphone.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
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
    private List<Error> messageError = new ArrayList<>();

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
        return new ErrorMessageException(messageError.get(0).title, getErrorKey());
    }

    public static class Error {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("title")
        @Expose
        private String title;

        public String getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getTitle() {
            return title;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
