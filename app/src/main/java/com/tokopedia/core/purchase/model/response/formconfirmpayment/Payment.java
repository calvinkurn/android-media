package com.tokopedia.core.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class Payment {
    private static final String TAG = Payment.class.getSimpleName();

    @SerializedName("order_left_amount_idr")
    @Expose
    private String orderLeftAmountIdr;
    @SerializedName("order_deposit_used_idr")
    @Expose
    private String orderDepositUsedIdr;
    @SerializedName("order_invoice")
    @Expose
    private String orderInvoice;
    @SerializedName("order_payment_year")
    @Expose
    private String orderPaymentYear;
    @SerializedName("order_confirmation_code_idr")
    @Expose
    private String orderConfirmationCodeIdr;
    @SerializedName("order_grand_total_idr")
    @Expose
    private String orderGrandTotalIdr;
    @SerializedName("order_payment_month")
    @Expose
    private String orderPaymentMonth;
    @SerializedName("order_payment_amount")
    @Expose
    private String orderPaymentAmount;
    @SerializedName("order_left_amount")
    @Expose
    private String orderLeftAmount;
    @SerializedName("order_payment_day")
    @Expose
    private String orderPaymentDay;
    @SerializedName("order_confirmation_code")
    @Expose
    private String orderConfirmationCode;
    @SerializedName("order_payment_id")
    @Expose
    private String orderPaymentId;
    @SerializedName("order_deposit_used")
    @Expose
    private String orderDepositUsed;
    @SerializedName("order_depositable")
    @Expose
    private Integer orderDepositable;
    @SerializedName("order_grand_total")
    @Expose
    private String orderGrandTotal;

    public String getOrderLeftAmountIdr() {
        return orderLeftAmountIdr;
    }

    public void setOrderLeftAmountIdr(String orderLeftAmountIdr) {
        this.orderLeftAmountIdr = orderLeftAmountIdr;
    }

    public String getOrderDepositUsedIdr() {
        return orderDepositUsedIdr;
    }

    public void setOrderDepositUsedIdr(String orderDepositUsedIdr) {
        this.orderDepositUsedIdr = orderDepositUsedIdr;
    }

    public String getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(String orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

    public String getOrderPaymentYear() {
        return orderPaymentYear;
    }

    public void setOrderPaymentYear(String orderPaymentYear) {
        this.orderPaymentYear = orderPaymentYear;
    }

    public String getOrderConfirmationCodeIdr() {
        return orderConfirmationCodeIdr;
    }

    public void setOrderConfirmationCodeIdr(String orderConfirmationCodeIdr) {
        this.orderConfirmationCodeIdr = orderConfirmationCodeIdr;
    }

    public String getOrderGrandTotalIdr() {
        return orderGrandTotalIdr;
    }

    public void setOrderGrandTotalIdr(String orderGrandTotalIdr) {
        this.orderGrandTotalIdr = orderGrandTotalIdr;
    }

    public String getOrderPaymentMonth() {
        return orderPaymentMonth;
    }

    public void setOrderPaymentMonth(String orderPaymentMonth) {
        this.orderPaymentMonth = orderPaymentMonth;
    }

    public String getOrderPaymentAmount() {
        return orderPaymentAmount;
    }

    public void setOrderPaymentAmount(String orderPaymentAmount) {
        this.orderPaymentAmount = orderPaymentAmount;
    }

    public String getOrderLeftAmount() {
        return orderLeftAmount;
    }

    public void setOrderLeftAmount(String orderLeftAmount) {
        this.orderLeftAmount = orderLeftAmount;
    }

    public String getOrderPaymentDay() {
        return orderPaymentDay;
    }

    public void setOrderPaymentDay(String orderPaymentDay) {
        this.orderPaymentDay = orderPaymentDay;
    }

    public String getOrderConfirmationCode() {
        return orderConfirmationCode;
    }

    public void setOrderConfirmationCode(String orderConfirmationCode) {
        this.orderConfirmationCode = orderConfirmationCode;
    }

    public String getOrderPaymentId() {
        return orderPaymentId;
    }

    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    public String getOrderDepositUsed() {
        return orderDepositUsed;
    }

    public void setOrderDepositUsed(String orderDepositUsed) {
        this.orderDepositUsed = orderDepositUsed;
    }

    public Integer getOrderDepositable() {
        return orderDepositable;
    }

    public void setOrderDepositable(Integer orderDepositable) {
        this.orderDepositable = orderDepositable;
    }

    public String getOrderGrandTotal() {
        return orderGrandTotal;
    }

    public void setOrderGrandTotal(String orderGrandTotal) {
        this.orderGrandTotal = orderGrandTotal;
    }
}
