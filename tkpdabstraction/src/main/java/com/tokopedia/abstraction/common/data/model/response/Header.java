package com.tokopedia.abstraction.common.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Header {

    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("messages")
    @Expose
    private List<String> messages;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("error_code")
    @Expose
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public double getProcessTime() {
        return processTime;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getReason() {
        return reason;
    }

}