
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class OrderPayment {

    @SerializedName("payment_process_due_date")
    @Expose
    private String paymentProcessDueDate;
    @SerializedName("payment_komisi")
    @Expose
    private String paymentKomisi;
    @SerializedName("payment_verify_date")
    @Expose
    private String paymentVerifyDate;
    @SerializedName("payment_shipping_due_date")
    @Expose
    private String paymentShippingDueDate;
    @SerializedName("payment_process_day_left")
    @Expose
    private int paymentProcessDayLeft;
    @SerializedName("payment_gateway_id")
    @Expose
    private int paymentGatewayId;
    @SerializedName("payment_gateway_image")
    @Expose
    private String paymentGatewayImage;
    @SerializedName("payment_shipping_day_left")
    @Expose
    private int paymentShippingDayLeft;
    @SerializedName("payment_gateway_name")
    @Expose
    private String paymentGatewayName;

    public String getPaymentProcessDueDate() {
        return paymentProcessDueDate;
    }

    public void setPaymentProcessDueDate(String paymentProcessDueDate) {
        this.paymentProcessDueDate = paymentProcessDueDate;
    }

    public String getPaymentKomisi() {
        return paymentKomisi;
    }

    public void setPaymentKomisi(String paymentKomisi) {
        this.paymentKomisi = paymentKomisi;
    }

    public String getPaymentVerifyDate() {
        return paymentVerifyDate;
    }

    public void setPaymentVerifyDate(String paymentVerifyDate) {
        this.paymentVerifyDate = paymentVerifyDate;
    }

    public String getPaymentShippingDueDate() {
        return paymentShippingDueDate;
    }

    public void setPaymentShippingDueDate(String paymentShippingDueDate) {
        this.paymentShippingDueDate = paymentShippingDueDate;
    }

    public int getPaymentProcessDayLeft() {
        return paymentProcessDayLeft;
    }

    public void setPaymentProcessDayLeft(int paymentProcessDayLeft) {
        this.paymentProcessDayLeft = paymentProcessDayLeft;
    }

    public int getPaymentGatewayId() {
        return paymentGatewayId;
    }

    public void setPaymentGatewayId(int paymentGatewayId) {
        this.paymentGatewayId = paymentGatewayId;
    }

    public String getPaymentGatewayImage() {
        return paymentGatewayImage;
    }

    public void setPaymentGatewayImage(String paymentGatewayImage) {
        this.paymentGatewayImage = paymentGatewayImage;
    }

    public int getPaymentShippingDayLeft() {
        return paymentShippingDayLeft;
    }

    public void setPaymentShippingDayLeft(int paymentShippingDayLeft) {
        this.paymentShippingDayLeft = paymentShippingDayLeft;
    }

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

}
