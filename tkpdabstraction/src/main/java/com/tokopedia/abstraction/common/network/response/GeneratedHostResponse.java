package com.tokopedia.abstraction.common.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeneratedHostResponse implements Parcelable {
    @SerializedName("server_id")
    @Expose
    private int serverId;
    @SerializedName("upload_host")
    @Expose
    private String uploadHost;
    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError;

    protected GeneratedHostResponse(Parcel in) {
        serverId = in.readInt();
        uploadHost = in.readString();
        userId = in.readInt();
        message = in.readString();
        status = in.readString();
        messageError = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serverId);
        dest.writeString(uploadHost);
        dest.writeInt(userId);
        dest.writeString(message);
        dest.writeString(status);
        dest.writeStringList(messageError);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeneratedHostResponse> CREATOR = new Creator<GeneratedHostResponse>() {
        @Override
        public GeneratedHostResponse createFromParcel(Parcel in) {
            return new GeneratedHostResponse(in);
        }

        @Override
        public GeneratedHostResponse[] newArray(int size) {
            return new GeneratedHostResponse[size];
        }
    };

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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


}
