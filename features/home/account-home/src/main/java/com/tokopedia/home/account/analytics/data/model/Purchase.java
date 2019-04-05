
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchase {

    @SerializedName("reorder")
    @Expose
    private int purchaseReorder = 0;
    @SerializedName("paymentConfirm")
    @Expose
    private int purchasePaymentConfirm = 0;
    @SerializedName("orderStatus")
    @Expose
    private int purchaseOrderStatus = 0;
    @SerializedName("deliveryConfirm")
    @Expose
    private int purchaseDeliveryConfirm = 0;

    public int getPurchaseReorder() {
        return purchaseReorder;
    }

    public void setPurchaseReorder(int purchaseReorder) {
        this.purchaseReorder = purchaseReorder;
    }

    public int getPurchasePaymentConfirm() {
        return purchasePaymentConfirm;
    }

    public void setPurchasePaymentConfirm(int purchasePaymentConfirm) {
        this.purchasePaymentConfirm = purchasePaymentConfirm;
    }

    public int getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(int purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public int getPurchaseDeliveryConfirm() {
        return purchaseDeliveryConfirm;
    }

    public void setPurchaseDeliveryConfirm(int purchaseDeliveryConfirm) {
        this.purchaseDeliveryConfirm = purchaseDeliveryConfirm;
    }

}
