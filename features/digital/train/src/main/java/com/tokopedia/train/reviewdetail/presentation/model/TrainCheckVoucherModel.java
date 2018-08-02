package com.tokopedia.train.reviewdetail.presentation.model;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherModel {

    private String voucherCode;
    private String message;
    private double discountAmountPlain;
    private double cashbackAmountPlain;

    public TrainCheckVoucherModel(String voucherCode, String message, double discountAmountPlain, double cashbackAmountPlain) {
        this.voucherCode = voucherCode;
        this.message = message;
        this.discountAmountPlain = discountAmountPlain;
        this.cashbackAmountPlain = cashbackAmountPlain;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getMessage() {
        return message;
    }

    public double getDiscountAmountPlain() {
        return discountAmountPlain;
    }

    public double getCashbackAmountPlain() {
        return cashbackAmountPlain;
    }

}
