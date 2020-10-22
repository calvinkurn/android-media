package com.tokopedia.core.network.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 10/12/2015.
 */
public class GeneratedHost implements Parcelable {

    @SerializedName("server_id")
    @Expose
    int serverId;
    @SerializedName("upload_host")
    @Expose
    String uploadHost;
    @SerializedName("user_id")
    @Expose
    int userId;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.serverId);
        dest.writeString(this.uploadHost);
        dest.writeInt(this.userId);
        dest.writeString(this.message);
        dest.writeString(this.status);
        dest.writeStringList(this.messageError);
    }

    public GeneratedHost() {
    }

    protected GeneratedHost(Parcel in) {
        this.serverId = in.readInt();
        this.uploadHost = in.readString();
        this.userId = in.readInt();
        this.message = in.readString();
        this.status = in.readString();
        this.messageError = in.createStringArrayList();
    }

    public static final Creator<GeneratedHost> CREATOR = new Creator<GeneratedHost>() {
        @Override
        public GeneratedHost createFromParcel(Parcel source) {
            return new GeneratedHost(source);
        }

        @Override
        public GeneratedHost[] newArray(int size) {
            return new GeneratedHost[size];
        }
    };
}
