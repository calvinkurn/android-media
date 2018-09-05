package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class NotificationData {
    private static final String TAG = NotificationData.class.getSimpleName();

    @SerializedName("total_cart")
    @Expose
    private int totalCart;
    @SerializedName("sales")
    @Expose
    private Sales sales;
    @SerializedName("resolution")
    @Expose
    private int resolution;
    @SerializedName("resolution_as")
    @Expose
    private Resolution resolutionModel;
    @SerializedName("purchase")
    @Expose
    private Purchase purchase;
    @SerializedName("incr_notif")
    @Expose
    private int incrNotif;
    @SerializedName("inbox")
    @Expose
    private Inbox inbox;
    @SerializedName("total_notif")
    @Expose
    private int totalNotif;
    @SerializedName("buyer_order_status")
    @Expose
    private NotificationBuyerOrder buyerOrder;
    @SerializedName("seller_order_status")
    @Expose
    private NotificationSellerOrder sellerOrder;

    public int getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(int totalCart) {
        this.totalCart = totalCart;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getIncrNotif() {
        return incrNotif;
    }

    public void setIncrNotif(int incrNotif) {
        this.incrNotif = incrNotif;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
    }

    public int getTotalNotif() {
        return totalNotif;
    }

    public void setTotalNotif(int totalNotif) {
        this.totalNotif = totalNotif;
    }

    public Resolution getResolutionModel() {
        return resolutionModel;
    }

    public void setResolutionModel(Resolution resolutionModel) {
        this.resolutionModel = resolutionModel;
    }

    public NotificationBuyerOrder getBuyerOrder() {
        return buyerOrder;
    }

    public void setBuyerOrder(NotificationBuyerOrder buyerOrder) {
        this.buyerOrder = buyerOrder;
    }

    public NotificationSellerOrder getSellerOrder() {
        return sellerOrder;
    }

    public void setSellerOrder(NotificationSellerOrder sellerOrder) {
        this.sellerOrder = sellerOrder;
    }
}
