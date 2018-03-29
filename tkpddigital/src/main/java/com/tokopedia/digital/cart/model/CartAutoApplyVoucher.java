package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/29/18.
 */

public class CartAutoApplyVoucher implements Parcelable {
    private boolean success;
    private String code;
    private int isCoupon;
    private long discountAmount;
    private String title;
    private String messageSuccess;
    private long promoId;

    public CartAutoApplyVoucher() {
    }

    protected CartAutoApplyVoucher(Parcel in) {
        success = in.readByte() != 0;
        code = in.readString();
        isCoupon = in.readInt();
        discountAmount = in.readLong();
        title = in.readString();
        messageSuccess = in.readString();
        promoId = in.readLong();
    }

    public static final Creator<CartAutoApplyVoucher> CREATOR = new Creator<CartAutoApplyVoucher>() {
        @Override
        public CartAutoApplyVoucher createFromParcel(Parcel in) {
            return new CartAutoApplyVoucher(in);
        }

        @Override
        public CartAutoApplyVoucher[] newArray(int size) {
            return new CartAutoApplyVoucher[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public long getPromoId() {
        return promoId;
    }

    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeString(code);
        parcel.writeInt(isCoupon);
        parcel.writeLong(discountAmount);
        parcel.writeString(title);
        parcel.writeString(messageSuccess);
        parcel.writeLong(promoId);
    }
}
