package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 22/05/18.
 */

public class AutoApply {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("is_coupon")
    @Expose
    private int isCoupon;
    @SerializedName("discount_amount")
    @Expose
    private long discountAmount;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("discounted_amount")
    @Expose
    private long discountedAmount;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("title_description")
    @Expose
    private String titleDescription;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;
    @SerializedName("promo_id")
    @Expose
    private int promoId;

    public boolean getIsSuccess() {
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

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public long getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(long discountedAmount) {
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

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }
}
