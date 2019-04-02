package com.tokopedia.train.reviewdetail.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 23/07/18.
 */
public class TrainCheckVoucherEntity {

    @SerializedName("galaCode")
    @Expose
    private String galaCode;
    @SerializedName("discountAmount")
    @Expose
    private double discountAmount;
    @SerializedName("cashbackAmount")
    @Expose
    private double cashbackAmount;
    @SerializedName("successMessage")
    @Expose
    private String successMessage;
    @SerializedName("failureMessage")
    @Expose
    private String failureMessage;
    @SerializedName("isCoupon")
    @Expose
    private String isCoupon;
    @SerializedName("invoiceDescription")
    @Expose
    private String invoiceDescription;

    public String getGalaCode() {
        return galaCode;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getCashbackAmount() {
        return cashbackAmount;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public String getIsCoupon() {
        return isCoupon;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

}
