
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
    @SerializedName("resolution")
    @Expose
    private int resolution = 0;
    @SerializedName("shop_id")
    @Expose
    private int shopId = 0;
    @SerializedName("chat")
    @Expose
    private Chat chat = new Chat();

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

    public int getResolution() {
        return resolution;
    }

    public int getShopId() {
        return shopId;
    }

    public Chat getChat() {
        return chat;
    }

}
