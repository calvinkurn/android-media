package com.tokopedia.challenges.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.challenges.view.model.Me;
import com.tokopedia.challenges.view.model.upload.Collection;

import java.io.Serializable;

public class ChallengeSettings implements Parcelable {

    @SerializedName("UploadAllowed")
    private boolean uploadAllowed;

    @SerializedName("Me")
    private Me me;

    @SerializedName("Collection")
    private Collection collection;

    @SerializedName("AllowVideos")
    private boolean allowVideos;

    @SerializedName("AllowPhotos")
    private boolean allowPhotos;

    public void setUploadAllowed(boolean uploadAllowed) {
        this.uploadAllowed = uploadAllowed;
    }

    public boolean isUploadAllowed() {
        return uploadAllowed;
    }

    public void setMe(Me me) {
        this.me = me;
    }

    public Me getMe() {
        return me;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setAllowVideos(boolean allowVideos) {
        this.allowVideos = allowVideos;
    }

    public boolean isAllowVideos() {
        return allowVideos;
    }

    public void setAllowPhotos(boolean allowPhotos) {
        this.allowPhotos = allowPhotos;
    }

    public boolean isAllowPhotos() {
        return allowPhotos;
    }

    @Override
    public String toString() {
        return
                "ChallengeSettings{" +
                        "uploadAllowed = '" + uploadAllowed + '\'' +
                        ",me = '" + me + '\'' +
                        ",collection = '" + collection + '\'' +
                        ",allowVideos = '" + allowVideos + '\'' +
                        ",allowPhotos = '" + allowPhotos + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.uploadAllowed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowVideos ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowPhotos ? (byte) 1 : (byte) 0);
    }

    public ChallengeSettings() {
    }

    protected ChallengeSettings(Parcel in) {
        this.uploadAllowed = in.readByte() != 0;
        this.allowVideos = in.readByte() != 0;
        this.allowPhotos = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ChallengeSettings> CREATOR = new Parcelable.Creator<ChallengeSettings>() {
        @Override
        public ChallengeSettings createFromParcel(Parcel source) {
            return new ChallengeSettings(source);
        }

        @Override
        public ChallengeSettings[] newArray(int size) {
            return new ChallengeSettings[size];
        }
    };
}