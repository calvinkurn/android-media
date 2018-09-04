
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Promo {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discount_numeric")
    @Expose
    private int discountNumeric;
    @SerializedName("cashback")
    @Expose
    private String cashback;
    @SerializedName("cashback_numeric")
    @Expose
    private int cashbackNumeric;
    @SerializedName("message")
    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getDiscountNumeric() {
        return discountNumeric;
    }

    public void setDiscountNumeric(int discountNumeric) {
        this.discountNumeric = discountNumeric;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public int getCashbackNumeric() {
        return cashbackNumeric;
    }

    public void setCashbackNumeric(int cashbackNumeric) {
        this.cashbackNumeric = cashbackNumeric;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
