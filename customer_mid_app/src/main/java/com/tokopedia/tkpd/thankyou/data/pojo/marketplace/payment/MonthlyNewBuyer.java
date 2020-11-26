package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthlyNewBuyer {
    @SerializedName("order_id")
    @Expose
    long orderID;
    @SerializedName("payment_id")
    @Expose
    long paymentId;
    @SerializedName("is_monthly_first_transaction")
    @Expose
    String isMonthlyFirstTransaction;

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getIsMonthlyFirstTransaction() {
        return isMonthlyFirstTransaction;
    }

    public void setIsMonthlyFirstTransaction(String isMonthlyFirstTransaction) {
        this.isMonthlyFirstTransaction = isMonthlyFirstTransaction;
    }
}
