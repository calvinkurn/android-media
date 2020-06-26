package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class Purchase {

    @SerializedName("purchase_reorder")
    @Expose
    private int purchaseReorder;
    @SerializedName("purchase_payment_confirm")
    @Expose
    private int purchasePaymentConfirm;
    @SerializedName("purchase_payment_conf")
    @Expose
    private int purchasePaymentConf;
    @SerializedName("purchase_order_status")
    @Expose
    private int purchaseOrderStatus;
    @SerializedName("purchase_delivery_confirm")
    @Expose
    private int purchaseDeliveryConfirm;

    public int getPurchaseReorder() {
        return purchaseReorder;
    }

    public int getPurchasePaymentConfirm() {
        return purchasePaymentConfirm;
    }

    public int getPurchasePaymentConf() {
        return purchasePaymentConf;
    }

    public int getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public int getPurchaseDeliveryConfirm() {
        return purchaseDeliveryConfirm;
    }
}
