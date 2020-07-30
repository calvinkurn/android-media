package com.tokopedia.digital.newcart.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherAttributeDigital implements Parcelable {

    private int isCoupon;

    private String voucherCode;

    private long userId;

    private String message;

    private String title;

    private long discountAmountPlain;

    private long cashbackAmpountPlain;

    protected VoucherAttributeDigital(Parcel in) {
        isCoupon = in.readInt();
        voucherCode = in.readString();
        userId = in.readLong();
        message = in.readString();
        title = in.readString();
        discountAmountPlain = in.readLong();
        cashbackAmpountPlain = in.readLong();
    }

    public static final Creator<VoucherAttributeDigital> CREATOR = new Creator<VoucherAttributeDigital>() {
        @Override
        public VoucherAttributeDigital createFromParcel(Parcel in) {
            return new VoucherAttributeDigital(in);
        }

        @Override
        public VoucherAttributeDigital[] newArray(int size) {
            return new VoucherAttributeDigital[size];
        }
    };

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

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(isCoupon);
        parcel.writeString(voucherCode);
        parcel.writeLong(userId);
        parcel.writeString(message);
        parcel.writeString(title);
        parcel.writeLong(discountAmountPlain);
        parcel.writeLong(cashbackAmpountPlain);
    }
}
