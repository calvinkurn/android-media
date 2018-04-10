
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
    private Integer totalNotif;
    @SerializedName("total_cart")
    @Expose
    private Integer totalCart;
    @SerializedName("resolution")
    @Expose
    private Integer resolution;
    @SerializedName("user_satisfaction_survey")
    @Expose
    private Integer userSatisfactionSurvey;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("chat")
    @Expose
    private Chat chat;

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

    public Integer getTotalNotif() {
        return totalNotif;
    }

    public void setTotalNotif(Integer totalNotif) {
        this.totalNotif = totalNotif;
    }

    public Integer getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(Integer totalCart) {
        this.totalCart = totalCart;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Integer getUserSatisfactionSurvey() {
        return userSatisfactionSurvey;
    }

    public void setUserSatisfactionSurvey(Integer userSatisfactionSurvey) {
        this.userSatisfactionSurvey = userSatisfactionSurvey;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
