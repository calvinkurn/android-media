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

    public AutoApplyData() {
    }

    protected AutoApplyData(Parcel in) {
        success = in.readByte() != 0;
        code = in.readString();
        isCoupon = in.readInt();
        discountAmount = in.readInt();
        titleDescription = in.readString();
        messageSuccess = in.readString();
        promoId = in.readInt();
    }

    public static final Creator<AutoApplyData> CREATOR = new Creator<AutoApplyData>() {
        @Override
        public AutoApplyData createFromParcel(Parcel in) {
            return new AutoApplyData(in);
        }

        @Override
        public AutoApplyData[] newArray(int size) {
            return new AutoApplyData[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeString(code);
        parcel.writeInt(isCoupon);
        parcel.writeInt(discountAmount);
        parcel.writeString(titleDescription);
        parcel.writeString(messageSuccess);
        parcel.writeInt(promoId);
    }
}
