
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
    @SerializedName("user_satisfaction_survey")
    @Expose
    private int userSatisfactionSurvey;
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

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
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

    public void setResolutionAs(ResolutionAs resolutionAs) {
        this.resolutionAs = resolutionAs;
    }

    public int getTotalNotif() {
        return totalNotif;
    }

    public void setTotalNotif(int totalNotif) {
        this.totalNotif = totalNotif;
    }

    public int getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(int totalCart) {
        this.totalCart = totalCart;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getUserSatisfactionSurvey() {
        return userSatisfactionSurvey;
    }

    public void setUserSatisfactionSurvey(int userSatisfactionSurvey) {
        this.userSatisfactionSurvey = userSatisfactionSurvey;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getIncrNotif() {
        return incrNotif;
    }

    public void setIncrNotif(int incrNotif) {
        this.incrNotif = incrNotif;
    }
}
