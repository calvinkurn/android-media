package com.tokopedia.purchase_platform.features.checkout.domain.model.saveshipmentstate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class SaveShipmentStateData implements Parcelable {

    private boolean success;
    private String error;
    private String message;

    public SaveShipmentStateData() {
    }

    protected SaveShipmentStateData(Parcel in) {
        success = in.readByte() != 0;
        error = in.readString();
        message = in.readString();
    }

    public static final Creator<SaveShipmentStateData> CREATOR = new Creator<SaveShipmentStateData>() {
        @Override
        public SaveShipmentStateData createFromParcel(Parcel in) {
            return new SaveShipmentStateData(in);
        }

        @Override
        public SaveShipmentStateData[] newArray(int size) {
            return new SaveShipmentStateData[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(error);
        dest.writeString(message);
    }
}
