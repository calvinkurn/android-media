package com.tokopedia.digital.newcart.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public class OtpData implements Parcelable {
    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public OtpData() {
    }

    protected OtpData(Parcel in) {
        this.message = in.readString();
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<OtpData> CREATOR =
            new Parcelable.Creator<OtpData>() {
                @Override
                public OtpData createFromParcel(Parcel source) {
                    return new OtpData(source);
                }

                @Override
                public OtpData[] newArray(int size) {
                    return new OtpData[size];
                }
            };
}
