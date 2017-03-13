package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherAttributeDigital implements Parcelable {

    private String voucherCode;

    private long userId;

    private String message;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
        dest.writeString(this.voucherCode);
        dest.writeLong(this.userId);
        dest.writeString(this.message);
    }

    public VoucherAttributeDigital() {
    }

    protected VoucherAttributeDigital(Parcel in) {
        this.voucherCode = in.readString();
        this.userId = in.readLong();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<VoucherAttributeDigital> CREATOR = new Parcelable.Creator<VoucherAttributeDigital>() {
        @Override
        public VoucherAttributeDigital createFromParcel(Parcel source) {
            return new VoucherAttributeDigital(source);
        }

        @Override
        public VoucherAttributeDigital[] newArray(int size) {
            return new VoucherAttributeDigital[size];
        }
    };
}
