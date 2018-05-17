package com.tokopedia.events.domain.model;

/**
 * Created by pranaymohapatra on 19/04/18.
 */

public class CouponModel {
    private String promocodeStatus;

    private int promocodeCashback;

    private int promocodeDiscount;

    private boolean promocodeIsCoupon;

    private String promocode;

    private String promocodeFailureMessage;

    private String promocodeSuccessMessage;

    public String getPromocodeStatus() {
        return promocodeStatus;
    }

    public void setPromocodeStatus(String promocodeStatus) {
        this.promocodeStatus = promocodeStatus;
    }

    public int getPromocodeCashback() {
        return promocodeCashback;
    }

    public void setPromocodeCashback(int promocodeCashback) {
        this.promocodeCashback = promocodeCashback;
    }

    public int getPromocodeDiscount() {
        return promocodeDiscount;
    }

    public void setPromocodeDiscount(int promocodeDiscount) {
        this.promocodeDiscount = promocodeDiscount;
    }

    public boolean isPromocodeIsCoupon() {
        return promocodeIsCoupon;
    }

    public void setPromocodeIsCoupon(boolean promocodeIsCoupon) {
        this.promocodeIsCoupon = promocodeIsCoupon;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getPromocodeFailureMessage() {
        return promocodeFailureMessage;
    }

    public void setPromocodeFailureMessage(String promocodeFailureMessage) {
        this.promocodeFailureMessage = promocodeFailureMessage;
    }

    public String getPromocodeSuccessMessage() {
        return promocodeSuccessMessage;
    }

    public void setPromocodeSuccessMessage(String promocodeSuccessMessage) {
        this.promocodeSuccessMessage = promocodeSuccessMessage;
    }
}

