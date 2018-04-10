
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Purchase {

    @SerializedName("purchase_reorder")
    @Expose
    private Integer purchaseReorder;
    @SerializedName("purchase_payment_confirm")
    @Expose
    private Integer purchasePaymentConfirm;
    @SerializedName("purchase_order_status")
    @Expose
    private Integer purchaseOrderStatus;
    @SerializedName("purchase_delivery_confirm")
    @Expose
    private Integer purchaseDeliveryConfirm;

    public Integer getPurchaseReorder() {
        return purchaseReorder;
    }

    public void setPurchaseReorder(Integer purchaseReorder) {
        this.purchaseReorder = purchaseReorder;
    }

    public Integer getPurchasePaymentConfirm() {
        return purchasePaymentConfirm;
    }

    public void setPurchasePaymentConfirm(Integer purchasePaymentConfirm) {
        this.purchasePaymentConfirm = purchasePaymentConfirm;
    }

    public Integer getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(Integer purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public Integer getPurchaseDeliveryConfirm() {
        return purchaseDeliveryConfirm;
    }

    public void setPurchaseDeliveryConfirm(Integer purchaseDeliveryConfirm) {
        this.purchaseDeliveryConfirm = purchaseDeliveryConfirm;
    }

}
