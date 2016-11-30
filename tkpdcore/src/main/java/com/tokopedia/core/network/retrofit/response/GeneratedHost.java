package com.tokopedia.core.network.retrofit.response;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 10/12/2015.
 */
public class GeneratedHost implements Parcelable {
    private static final String TAG = GeneratedHost.class.getSimpleName();

    @SerializedName("server_id")
    @Expose
    int serverId;
    @SerializedName("upload_host")
    @Expose
    String uploadHost;
    @SerializedName("user_id")
    @Expose
    int userId;

    protected GeneratedHost(android.os.Parcel in) {
        serverId = in.readInt();
        uploadHost = in.readString();
        userId = in.readInt();
    }

    public static final Creator<GeneratedHost> CREATOR = new Creator<GeneratedHost>() {
        @Override
        public GeneratedHost createFromParcel(android.os.Parcel in) {
            return new GeneratedHost(in);
        }

        @Override
        public GeneratedHost[] newArray(int size) {
            return new GeneratedHost[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(serverId);
        dest.writeString(uploadHost);
        dest.writeInt(userId);
    }
}
