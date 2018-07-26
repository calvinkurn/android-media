package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 1/23/18.
 */

public class PaymentInfoEntity {
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;
    @SerializedName("gateway_icon")
    @Expose
    private String gatewayIcon;
    @SerializedName("expire_on")
    @Expose
    private String expireOn;
    @SerializedName("transaction_code")
    @Expose
    private String transactionCode;
    @SerializedName("total_amt")
    @Expose
    private int totalAmount;
    @SerializedName("manual_transfer")
    @Expose
    private ManualTransferEntity manualTransfer;

    public String getGatewayName() {
        return gatewayName;
    }

    public String getGatewayIcon() {
        return gatewayIcon;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public ManualTransferEntity getManualTransfer() {
        return manualTransfer;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
