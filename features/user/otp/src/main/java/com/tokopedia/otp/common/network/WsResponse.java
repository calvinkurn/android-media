package com.tokopedia.otp.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 4/27/18.
 */

public class WsResponse<T> {

    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("message_status")
    @Expose
    private List<String> messageStatus = null;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError = null;

    public T getData() {
        return data;
    }

    public List<String> getMessageStatus() {
        return messageStatus;
    }

    public List<String> getMessageError() {
        return messageError;
    }
}
