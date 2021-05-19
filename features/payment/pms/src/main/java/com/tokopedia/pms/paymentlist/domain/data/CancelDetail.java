
package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelDetail {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("hasRefund")
    @Expose
    private boolean hasRefund;
    @SerializedName("refundCCAmount")
    @Expose
    private int refundCCAmount;
    @SerializedName("refundWalletAmount")
    @Expose
    private int refundWalletAmount;
    @SerializedName("refundMessage")
    @Expose
    private String refundMessage;
    @SerializedName("combineMessage")
    @Expose
    String combineMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(boolean hasRefund) {
        this.hasRefund = hasRefund;
    }

    public int getRefundCCAmount() {
        return refundCCAmount;
    }

    public void setRefundCCAmount(int refundCCAmount) {
        this.refundCCAmount = refundCCAmount;
    }

    public int getRefundWalletAmount() {
        return refundWalletAmount;
    }

    public void setRefundWalletAmount(int refundWalletAmount) {
        this.refundWalletAmount = refundWalletAmount;
    }

    public String getRefundMessage() {
        return refundMessage;
    }

    public void setRefundMessage(String refundMessage) {
        this.refundMessage = refundMessage;
    }

    public String getCombineMessage() {
        return combineMessage;
    }

    public void setCombineMessage(String combineMessage) { this.combineMessage = combineMessage; }

}
