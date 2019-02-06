package com.tokopedia.core.gcm.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author  by alvarisi on 12/27/16.
 */

public class FCMTokenUpdateEntity implements Parcelable {
    private String token;
    private Boolean isSuccess;

    public FCMTokenUpdateEntity() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    protected FCMTokenUpdateEntity(Parcel in) {
        token = in.readString();
        byte isSuccessVal = in.readByte();
        isSuccess = isSuccessVal == 0x02 ? null : isSuccessVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        if (isSuccess == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isSuccess ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<FCMTokenUpdateEntity> CREATOR = new Creator<FCMTokenUpdateEntity>() {
        @Override
        public FCMTokenUpdateEntity createFromParcel(Parcel in) {
            return new FCMTokenUpdateEntity(in);
        }

        @Override
        public FCMTokenUpdateEntity[] newArray(int size) {
            return new FCMTokenUpdateEntity[size];
        }
    };

    @Override
    public String toString() {
        return "FCMTokenUpdateEntity{" +
                "token='" + token + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
