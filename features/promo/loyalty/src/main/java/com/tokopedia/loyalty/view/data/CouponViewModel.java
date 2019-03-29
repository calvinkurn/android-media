package com.tokopedia.loyalty.view.data;

/**
 * Created by kris on 12/6/17. Tokopedia
 */

public class CouponViewModel {

    private boolean isSuccess;

    private String message;

    private String amount;

    private String code;

    private String title;

    private long rawDiscount;

    private long rawCashback;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRawDiscount() {
        return rawDiscount;
    }

    public void setRawDiscount(long rawDiscount) {
        this.rawDiscount = rawDiscount;
    }

    public long getRawCashback() {
        return rawCashback;
    }

    public void setRawCashback(long rawCashback) {
        this.rawCashback = rawCashback;
    }
}
