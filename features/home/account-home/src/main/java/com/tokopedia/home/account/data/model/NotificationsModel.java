package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class NotificationsModel {
    @SerializedName("purchase")
    @Expose
    private NotificationPurchaseModel purchase;
    @SerializedName("sales")
    @Expose
    private NotificationSalesModel sales;

    public NotificationPurchaseModel getPurchase() {
        return purchase;
    }

    public void setPurchase(NotificationPurchaseModel purchase) {
        this.purchase = purchase;
    }

    public NotificationSalesModel getSales() {
        return sales;
    }

    public void setSales(NotificationSalesModel sales) {
        this.sales = sales;
    }
}
