package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherAttributeDigital implements Parcelable {

    private String voucherCode;

    private long userId;

    private String message;

    private long discountAmountPlain;

    private long cashbackAmpountPlain;

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

    public long getDiscountAmountPlain() {
        return discountAmountPlain;
    }

    public void setDiscountAmountPlain(long discountAmountPlain) {
        this.discountAmountPlain = discountAmountPlain;
    }

    public long getCashbackAmpountPlain() {
        return cashbackAmpountPlain;
    }

    public void setCashbackAmpountPlain(long cashbackAmpountPlain) {
        this.cashbackAmpountPlain = cashbackAmpountPlain;
    }

    public VoucherAttributeDigital() {
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
        dest.writeLong(this.discountAmountPlain);
        dest.writeLong(this.cashbackAmpountPlain);
    }

    protected VoucherAttributeDigital(Parcel in) {
        this.voucherCode = in.readString();
        this.userId = in.readLong();
        this.message = in.readString();
        this.discountAmountPlain = in.readLong();
        this.cashbackAmpountPlain = in.readLong();
    }

    public static final Creator<VoucherAttributeDigital> CREATOR = new Creator<VoucherAttributeDigital>() {
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
