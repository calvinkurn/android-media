package com.tokopedia.train.reviewdetail.presentation.model;

public class TrainCheckVoucherModel {

    private String voucherCode;
    private String message;
    private double discountAmountPlain;
    private double cashbackAmountPlain;

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
