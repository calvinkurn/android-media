package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Irfan Khoirul on 17/05/18.
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
    private int discountAmount;
    @SerializedName("title_description")
    @Expose
    private String titleDescription;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;
    @SerializedName("promo_id")
    @Expose
    private int promoId;
    @SerializedName("message")
    @Expose
    private MessageAutoApply messageAutoApply;

    public MessageAutoApply getMessageAutoApply() {
        return messageAutoApply;
    }

    public void setMessageAutoApply(MessageAutoApply messageAutoApply) {
        this.messageAutoApply = messageAutoApply;
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
}
