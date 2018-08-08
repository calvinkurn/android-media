package com.tokopedia.abstraction.common.network.exception;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Header implements Parcelable {

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

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Header() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.processTime);
        dest.writeStringList(this.messages);
        dest.writeString(this.reason);
        dest.writeString(this.errorCode);
    }

    protected Header(Parcel in) {
        this.processTime = in.readDouble();
        this.messages = in.createStringArrayList();
        this.reason = in.readString();
        this.errorCode = in.readString();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };
}