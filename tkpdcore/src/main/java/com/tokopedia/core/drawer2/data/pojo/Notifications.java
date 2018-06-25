
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("sales")
    @Expose
    private Sales sales;
    @SerializedName("inbox")
    @Expose
    private Inbox inbox;
    @SerializedName("purchase")
    @Expose
    private Purchase purchase;
    @SerializedName("resolution_as")
    @Expose
    private ResolutionAs resolutionAs;
    @SerializedName("total_notif")
    @Expose
    private int totalNotif;
    @SerializedName("total_cart")
    @Expose
    private int totalCart;
    @SerializedName("resolution")
    @Expose
    private int resolution;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("chat")
    @Expose
    private Chat chat;
    @SerializedName("incr_notif")
    @Expose
    private int incrNotif;

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
