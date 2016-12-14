package com.tokopedia.core.gcm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class FCMTokenUpdate implements Parcelable{
    private String mOldToken;
    private String mNewToken;
    private String mOsType;
    private String mAccessToken;
    private String mUserId;

    public FCMTokenUpdate() {
    }


    protected FCMTokenUpdate(Parcel in) {
        mOldToken = in.readString();
        mNewToken = in.readString();
        mOsType = in.readString();
        mAccessToken = in.readString();
        mUserId = in.readString();
    }

    public static final Creator<FCMTokenUpdate> CREATOR = new Creator<FCMTokenUpdate>() {
        @Override
        public FCMTokenUpdate createFromParcel(Parcel in) {
            return new FCMTokenUpdate(in);
        }

        @Override
        public FCMTokenUpdate[] newArray(int size) {
            return new FCMTokenUpdate[size];
        }
    };

    public String getOldToken() {
        return mOldToken;
    }

    public void setOldToken(String oldToken) {
        mOldToken = oldToken;
    }

    public String getNewToken() {
        return mNewToken;
    }

    public void setNewToken(String newToken) {
        mNewToken = newToken;
    }

    public String getOsType() {
        return mOsType;
    }

    public void setOsType(String osType) {
        mOsType = osType;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }


    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOldToken);
        dest.writeString(mNewToken);
        dest.writeString(mOsType);
        dest.writeString(mAccessToken);
        dest.writeString(mUserId);
    }
}
