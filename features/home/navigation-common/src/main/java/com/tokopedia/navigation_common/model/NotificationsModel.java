package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.navigation_common.model.inboxcounter.InboxCounter;

/**
 * @author okasurya on 7/21/18.
 */
public class NotificationsModel {
    @SerializedName("resolutionAs")
    @Expose
    private NotificationResolutionModel resolution = new NotificationResolutionModel();
    @SerializedName("buyerOrderStatus")
    @Expose
    private NotificationBuyerOrderModel buyerOrder = new NotificationBuyerOrderModel();
    @SerializedName("sellerOrderStatus")
    @Expose
    private NotificationSellerOrderModel sellerOrder = new NotificationSellerOrderModel();
    @SerializedName("sellerInfo")
    @Expose
    private NotificationSellerInfoModel sellerInfo = new NotificationSellerInfoModel();

    @SerializedName("total_cart")
    @Expose
    private String totalCart = "";

    @SerializedName("inbox")
    @Expose
    private InboxModel inbox = new InboxModel();

    @SerializedName("inbox_counter")
    @Expose
    private InboxCounter inboxCounter = new InboxCounter();

    @SerializedName("chat")
    @Expose
    private ChatModel chat = new ChatModel();

    public NotificationResolutionModel getResolution() {
        return resolution;
    }

    public void setResolution(NotificationResolutionModel resolution) {
        this.resolution = resolution;
    }

    public NotificationBuyerOrderModel getBuyerOrder() {
        return buyerOrder;
    }

    public void setBuyerOrder(NotificationBuyerOrderModel buyerOrder) {
        this.buyerOrder = buyerOrder;
    }

    public NotificationSellerOrderModel getSellerOrder() {
        return sellerOrder;
    }

    public void setSellerOrder(NotificationSellerOrderModel sellerOrder) {
        this.sellerOrder = sellerOrder;
    }

    public NotificationSellerInfoModel getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(NotificationSellerInfoModel sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public String getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(String totalCart) {
        this.totalCart = totalCart;
    }

    public InboxModel getInbox() {
        return inbox;
    }

    public void setInbox(InboxModel inbox) {
        this.inbox = inbox;
    }

    public ChatModel getChat() {
        return chat;
    }

    public void setChat(ChatModel chat) {
        this.chat = chat;
    }

    public int getTotalNewInbox() {
        return inboxCounter.all.totalInt;
    }

    public int getTotalNotificationOnNewInbox() {
        return inboxCounter.all.notifcenterInt;
    }
}
