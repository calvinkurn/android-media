package com.tokopedia.flight.bookingV2.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 22/05/18.
 */

public class FlightBookingVoucherViewModel implements Parcelable {

    public static final String COUPON = "coupon";

    private boolean enableVoucher;
    private int isCouponActive;
    private boolean autoapplySuccess;
    private String code;
    private int isCoupon;
    private double discountAmount;
    private String discountPrice;
    private double discountedAmount;
    private String discountedPrice;
    private String titleDescription;
    private String messageSuccess;
    private int promoId;
    private String defaultPromoTab;

    public FlightBookingVoucherViewModel() {
    }

    protected FlightBookingVoucherViewModel(Parcel in) {
        enableVoucher = in.readByte() != 0;
        isCouponActive = in.readInt();
        autoapplySuccess = in.readByte() != 0;
        code = in.readString();
        isCoupon = in.readInt();
        discountAmount = in.readDouble();
        discountPrice = in.readString();
        discountedAmount = in.readDouble();
        discountedPrice = in.readString();
        titleDescription = in.readString();
        messageSuccess = in.readString();
        promoId = in.readInt();
        defaultPromoTab = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enableVoucher ? 1 : 0));
        dest.writeInt(isCouponActive);
        dest.writeByte((byte) (autoapplySuccess ? 1 : 0));
        dest.writeString(code);
        dest.writeInt(isCoupon);
        dest.writeDouble(discountAmount);
        dest.writeString(discountPrice);
        dest.writeDouble(discountedAmount);
        dest.writeString(discountedPrice);
        dest.writeString(titleDescription);
        dest.writeString(messageSuccess);
        dest.writeInt(promoId);
        dest.writeString(defaultPromoTab);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightBookingVoucherViewModel> CREATOR = new Creator<FlightBookingVoucherViewModel>() {
        @Override
        public FlightBookingVoucherViewModel createFromParcel(Parcel in) {
            return new FlightBookingVoucherViewModel(in);
        }

        @Override
        public FlightBookingVoucherViewModel[] newArray(int size) {
            return new FlightBookingVoucherViewModel[size];
        }
    };

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }

    public int getIsCouponActive() {
        return isCouponActive;
    }

    public void setIsCouponActive(int isCouponActive) {
        this.isCouponActive = isCouponActive;
    }

    public boolean isAutoapplySuccess() {
        return autoapplySuccess;
    }

    public void setAutoapplySuccess(boolean autoapplySuccess) {
        this.autoapplySuccess = autoapplySuccess;
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

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(double discountedAmount) {
        this.discountedAmount = discountedAmount;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
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

    public String getDefaultPromoTab() {
        return defaultPromoTab;
    }

    public void setDefaultPromoTab(String defaultPromoTab) {
        this.defaultPromoTab = defaultPromoTab;
    }
}
