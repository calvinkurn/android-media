package com.tokopedia.core.gcm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class FcmTokenUpdate implements Parcelable{
    private String mOldToken;
    private String mNewToken;
    private String mOsType;

    protected FcmTokenUpdate(Parcel in) {
        mOldToken = in.readString();
        mNewToken = in.readString();
        mOsType = in.readString();
    }

    public static final Creator<FcmTokenUpdate> CREATOR = new Creator<FcmTokenUpdate>() {
        @Override
        public FcmTokenUpdate createFromParcel(Parcel in) {
            return new FcmTokenUpdate(in);
        }

        @Override
        public FcmTokenUpdate[] newArray(int size) {
            return new FcmTokenUpdate[size];
        }
    };

    public FcmTokenUpdate() {
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOldToken);
        dest.writeString(mNewToken);
        dest.writeString(mOsType);
    }
}
