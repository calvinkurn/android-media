
package com.tokopedia.flight.review.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributesVoucher {

    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("discount_amount_plain")
    @Expose
    private double discountAmountPlain;
    @SerializedName("cashback_amount")
    @Expose
    private String cashbackAmount;
    @SerializedName("cashback_amount_plain")
    @Expose
    private int cashbackAmountPlain;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("discounted_price_plain")
    @Expose
    private double discountedPricePlain;
    @SerializedName("message")
    @Expose
    private String message;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountAmountPlain() {
        return discountAmountPlain;
    }

    public void setDiscountAmountPlain(double discountAmountPlain) {
        this.discountAmountPlain = discountAmountPlain;
    }

    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(String cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public int getCashbackAmountPlain() {
        return cashbackAmountPlain;
    }

    public void setCashbackAmountPlain(int cashbackAmountPlain) {
        this.cashbackAmountPlain = cashbackAmountPlain;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public double getDiscountedPricePlain() {
        return discountedPricePlain;
    }

    public void setDiscountedPricePlain(double discountedPricePlain) {
        this.discountedPricePlain = discountedPricePlain;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
