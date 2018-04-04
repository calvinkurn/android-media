package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

public class AttributesDigital implements Parcelable {

    private String userId;

    private String clientNumber;

    private String icon;

    private String price;

    private String categoryName;

    private String operatorName;

    private long pricePlain;

    private boolean instantCheckout;

    private boolean needOtp;

    private String smsState;

    private boolean enableVoucher;

    private String voucherAutoCode;

    private int isCouponActive;

    private UserInputPriceDigital userInputPrice;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(long pricePlain) {
        this.pricePlain = pricePlain;
    }

    public UserInputPriceDigital getUserInputPrice() {
        return userInputPrice;
    }

    public void setUserInputPrice(UserInputPriceDigital userInputPrice) {
        this.userInputPrice = userInputPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public boolean isNeedOtp() {
        return needOtp;
    }

    public void setNeedOtp(boolean needOtp) {
        this.needOtp = needOtp;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }

    public String getVoucherAutoCode() {
        return voucherAutoCode;
    }

    public int isCouponActive() {
        return isCouponActive;
    }

    public void setIsCouponActive(int isCouponActive) {
        this.isCouponActive = isCouponActive;
    }

    public void setVoucherAutoCode(String voucherAutoCode) {
        this.voucherAutoCode = voucherAutoCode;
    }

    public AttributesDigital() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.clientNumber);
        dest.writeString(this.icon);
        dest.writeString(this.price);
        dest.writeString(this.categoryName);
        dest.writeString(this.operatorName);
        dest.writeLong(this.pricePlain);
        dest.writeByte(this.instantCheckout ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needOtp ? (byte) 1 : (byte) 0);
        dest.writeString(this.smsState);
        dest.writeByte(this.enableVoucher ? (byte) 1 : (byte) 0);
        dest.writeString(this.voucherAutoCode);
        dest.writeInt(this.isCouponActive);
        dest.writeParcelable(this.userInputPrice, flags);
    }

    protected AttributesDigital(Parcel in) {
        this.userId = in.readString();
        this.clientNumber = in.readString();
        this.icon = in.readString();
        this.price = in.readString();
        this.categoryName = in.readString();
        this.operatorName = in.readString();
        this.pricePlain = in.readLong();
        this.instantCheckout = in.readByte() != 0;
        this.needOtp = in.readByte() != 0;
        this.smsState = in.readString();
        this.enableVoucher = in.readByte() != 0;
        this.voucherAutoCode = in.readString();
        this.isCouponActive = in.readInt();
        this.userInputPrice = in.readParcelable(UserInputPriceDigital.class.getClassLoader());
    }

    public static final Creator<AttributesDigital> CREATOR = new Creator<AttributesDigital>() {
        @Override
        public AttributesDigital createFromParcel(Parcel source) {
            return new AttributesDigital(source);
        }

        @Override
        public AttributesDigital[] newArray(int size) {
            return new AttributesDigital[size];
        }
    };
}
