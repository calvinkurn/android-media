package com.tokopedia.digital.cart.model;

/**
 * Created by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherAttributeDigital {

    private String voucherCode;

    private long userId;

    private String message;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
