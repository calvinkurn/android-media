package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Irfan Khoirul on 17/05/18.
 */

public class AutoApplyData implements Parcelable {

    private boolean success;
    private String code;
    private int isCoupon;
    private int discountAmount;
    private String titleDescription;
    private String messageSuccess;
    private int promoId;
    private String state;

    public AutoApplyData() {
    }

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

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.code);
        dest.writeInt(this.isCoupon);
        dest.writeInt(this.discountAmount);
        dest.writeString(this.titleDescription);
        dest.writeString(this.messageSuccess);
        dest.writeInt(this.promoId);
        dest.writeString(this.state);
    }

    protected AutoApplyData(Parcel in) {
        this.success = in.readByte() != 0;
        this.code = in.readString();
        this.isCoupon = in.readInt();
        this.discountAmount = in.readInt();
        this.titleDescription = in.readString();
        this.messageSuccess = in.readString();
        this.promoId = in.readInt();
        this.state = in.readString();
    }

    public static final Creator<AutoApplyData> CREATOR = new Creator<AutoApplyData>() {
        @Override
        public AutoApplyData createFromParcel(Parcel source) {
            return new AutoApplyData(source);
        }

        @Override
        public AutoApplyData[] newArray(int size) {
            return new AutoApplyData[size];
        }
    };
}
