package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {

    @SerializedName("total_price")
    private Integer totalPrice;

    @SerializedName("promocode_success_message")
    private String promocodeSuccessMessage;

    @SerializedName("cart_items")
    private List<CartItemsItem> cartItems;

    @SerializedName("promocode_failure_message")
    private String promocodeFailureMessage;

    @SerializedName("count")
    private Integer count;

    @SerializedName("total_conv_fee")
    private Integer totalConvFee;

    @SerializedName("promocode")
    private String promocode;

    @SerializedName("error")
    private String error;

    @SerializedName("promocode_status")
    private String promocodeStatus;

    @SerializedName("promocode_cashback")
    private Integer promocodeCashback;

    @SerializedName("promocode_discount")
    private Integer promocodeDiscount;

    @SerializedName("promocode_is_coupon")
    private boolean promocodeIsCoupon;

    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("grand_total")
    private Integer grandTotal;

    @SerializedName("user")
    private User user;

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setPromocodeSuccessMessage(String promocodeSuccessMessage) {
        this.promocodeSuccessMessage = promocodeSuccessMessage;
    }

    public String getPromocodeSuccessMessage() {
        return promocodeSuccessMessage;
    }

    public void setCartItems(List<CartItemsItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItemsItem> getCartItems() {
        return cartItems;
    }

    public void setPromocodeFailureMessage(String promocodeFailureMessage) {
        this.promocodeFailureMessage = promocodeFailureMessage;
    }

    public String getPromocodeFailureMessage() {
        return promocodeFailureMessage;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setTotalConvFee(Integer totalConvFee) {
        this.totalConvFee = totalConvFee;
    }

    public Integer getTotalConvFee() {
        return totalConvFee;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setPromocodeStatus(String promocodeStatus) {
        this.promocodeStatus = promocodeStatus;
    }

    public String getPromocodeStatus() {
        return promocodeStatus;
    }

    public void setPromocodeCashback(Integer promocodeCashback) {
        this.promocodeCashback = promocodeCashback;
    }

    public Integer getPromocodeCashback() {
        return promocodeCashback;
    }

    public void setPromocodeDiscount(Integer promocodeDiscount) {
        this.promocodeDiscount = promocodeDiscount;
    }

    public boolean isPromocodeIsCoupon() {
        return promocodeIsCoupon;
    }

    public void setPromocodeIsCoupon(boolean promocodeIsCoupon) {
        this.promocodeIsCoupon = promocodeIsCoupon;
    }

    public Integer getPromocodeDiscount() {
        return promocodeDiscount;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return
                "Cart{" +
                        "total_price = '" + totalPrice + '\'' +
                        ",promocode_success_message = '" + promocodeSuccessMessage + '\'' +
                        ",cart_items = '" + cartItems + '\'' +
                        ",promocode_failure_message = '" + promocodeFailureMessage + '\'' +
                        ",count = '" + count + '\'' +
                        ",total_conv_fee = '" + totalConvFee + '\'' +
                        ",promocode = '" + promocode + '\'' +
                        ",error = '" + error + '\'' +
                        ",promocode_status = '" + promocodeStatus + '\'' +
                        ",promocode_cashback = '" + promocodeCashback + '\'' +
                        ",promocode_discount = '" + promocodeDiscount + '\'' +
                        ",error_code = '" + errorCode + '\'' +
                        ",grand_total = '" + grandTotal + '\'' +
                        ",user = '" + user + '\'' +
                        "}";
    }
}