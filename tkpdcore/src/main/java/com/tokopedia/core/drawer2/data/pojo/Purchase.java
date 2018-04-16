
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchase {

    @SerializedName("purchase_reorder")
    @Expose
    private int purchaseReorder;
    @SerializedName("purchase_payment_confirm")
    @Expose
    private int purchasePaymentConfirm;
    @SerializedName("purchase_order_status")
    @Expose
    private int purchaseOrderStatus;
    @SerializedName("purchase_delivery_confirm")
    @Expose
    private int purchaseDeliveryConfirm;

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
