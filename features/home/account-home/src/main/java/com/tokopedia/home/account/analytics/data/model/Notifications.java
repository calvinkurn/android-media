
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("sales")
    @Expose
    private Sales sales = new Sales();
    @SerializedName("inbox")
    @Expose
    private Inbox inbox = new Inbox();
    @SerializedName("purchase")
    @Expose
    private Purchase purchase = new Purchase();
    @SerializedName("resolution_as")
    @Expose
    private ResolutionAs resolutionAs = new ResolutionAs();
    @SerializedName("total_notif")
    @Expose
    private int totalNotif = 0;
    @SerializedName("total_cart")
    @Expose
    private int totalCart = 0;
    @SerializedName("resolution")
    @Expose
    private int resolution = 0;
    @SerializedName("shop_id")
    @Expose
    private int shopId = 0;
    @SerializedName("chat")
    @Expose
    private Chat chat = new Chat();
    @SerializedName("incr_notif")
    @Expose
    private int incrNotif = 0;

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public ResolutionAs getResolutionAs() {
        return resolutionAs;
    }

    public int getTotalNotif() {
        return totalNotif;
    }

    public int getTotalCart() {
        return totalCart;
    }

    public int getResolution() {
        return resolution;
    }

    public int getShopId() {
        return shopId;
    }

    public Chat getChat() {
        return chat;
    }

    public int getIncrNotif() {
        return incrNotif;
    }

}
