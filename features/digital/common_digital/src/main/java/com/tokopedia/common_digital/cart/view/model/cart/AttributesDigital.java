package com.tokopedia.common_digital.cart.view.model.cart;

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

    private CartAutoApplyVoucher autoApplyVoucher;

    private String defaultPromoTab;

    protected AttributesDigital(Parcel in) {
        userId = in.readString();
        clientNumber = in.readString();
        icon = in.readString();
        price = in.readString();
        categoryName = in.readString();
        operatorName = in.readString();
        pricePlain = in.readLong();
        instantCheckout = in.readByte() != 0;
        needOtp = in.readByte() != 0;
        smsState = in.readString();
        enableVoucher = in.readByte() != 0;
        voucherAutoCode = in.readString();
        isCouponActive = in.readInt();
        userInputPrice = in.readParcelable(UserInputPriceDigital.class.getClassLoader());
        autoApplyVoucher = in.readParcelable(CartAutoApplyVoucher.class.getClassLoader());
        defaultPromoTab = in.readString();
    }

    public static final Creator<AttributesDigital> CREATOR = new Creator<AttributesDigital>() {
        @Override
        public AttributesDigital createFromParcel(Parcel in) {
            return new AttributesDigital(in);
        }

        @Override
        public AttributesDigital[] newArray(int size) {
            return new AttributesDigital[size];
        }
    };

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

    public CartAutoApplyVoucher getAutoApplyVoucher() {
        return autoApplyVoucher;
    }

    public void setAutoApplyVoucher(CartAutoApplyVoucher autoApplyVoucher) {
        this.autoApplyVoucher = autoApplyVoucher;
    }

    public String getDefaultPromoTab() {
        return defaultPromoTab;
    }

    public void setDefaultPromoTab(String defaultPromoTab) {
        this.defaultPromoTab = defaultPromoTab;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(clientNumber);
        dest.writeString(icon);
        dest.writeString(price);
        dest.writeString(categoryName);
        dest.writeString(operatorName);
        dest.writeLong(pricePlain);
        dest.writeByte((byte) (instantCheckout ? 1 : 0));
        dest.writeByte((byte) (needOtp ? 1 : 0));
        dest.writeString(smsState);
        dest.writeByte((byte) (enableVoucher ? 1 : 0));
        dest.writeString(voucherAutoCode);
        dest.writeInt(isCouponActive);
        dest.writeParcelable(userInputPrice, flags);
        dest.writeParcelable(autoApplyVoucher, flags);
        dest.writeString(defaultPromoTab);
    }
}
