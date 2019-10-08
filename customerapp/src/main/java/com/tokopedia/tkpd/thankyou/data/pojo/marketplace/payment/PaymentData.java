package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 12/7/17.
 */

public class PaymentData {
//    @SerializedName("create_time")
//    @Expose
//    private String createTime;
//    @SerializedName("customer_id")
//    @Expose
//    private int customerId;
//    @SerializedName("discount_amount")
//    @Expose
//    private float discountAmount;
    @SerializedName("fee_amount")
    @Expose
    private float feeAmount;
//    @SerializedName("order_amount")
//    @Expose
//    private float orderAmount;
//    @SerializedName("overpaid_amount")
//    @Expose
//    private float overpaidAmount;
//    @SerializedName("partial")
//    @Expose
//    private Partial partial;
    @SerializedName("payment_amount")
    @Expose
    private float paymentAmount;
//    @SerializedName("payment_date")
//    @Expose
//    private String paymentDate;
    @SerializedName("payment_gateway")
    @Expose
    private PaymentGateway paymentGateway;
    @SerializedName("payment_id")
    @Expose
    private int paymentId;
    @SerializedName("payment_method")
    @Expose
    private PaymentMethod paymentMethod;
    @SerializedName("payment_ref_num")
    @Expose
    private String paymentRefNum;
//    @SerializedName("payment_status")
//    @Expose
//    private String paymentStatus;
//    @SerializedName("payment_status_int")
//    @Expose
//    private int paymentStatusInt;
    @SerializedName("payment_type")
    @Expose
    private PaymentType paymentType;
//    @SerializedName("saldo_amount")
//    @Expose
//    private float saldoAmount;
    @SerializedName("stacked_promos")
    @Expose
    private StackedPromos stackedPromos;
//    @SerializedName("update_time")
//    @Expose
//    private String updateTime;
    @SerializedName("voucher")
    @Expose
    private Voucher voucher;
//    @SerializedName("orders")
//    @Expose
    private List<OrderData> orders;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentRefNum() {
        return paymentRefNum;
    }

    public void setPaymentRefNum(String paymentRefNum) {
        this.paymentRefNum = paymentRefNum;
    }

    public List<OrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderData> orders) {
        this.orders = orders;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public float getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(float feeAmount) {
        this.feeAmount = feeAmount;
    }

    public PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public StackedPromos getStackedPromos() {
        return stackedPromos;
    }
}
