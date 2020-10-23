
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxReputation {

    @SerializedName("inbox_id")
    @Expose
    private int inboxId;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("reputation_id")
    @Expose
    private int reputationId;
    @SerializedName("order_data")
    @Expose
    private OrderData orderData;
    @SerializedName("reviewee_data")
    @Expose
    private RevieweeData revieweeData;
    @SerializedName("reputation_data")
    @Expose
    private ReputationData reputationData;

    public int getInboxId() {
        return inboxId;
    }

    public void setInboxId(int inboxId) {
        this.inboxId = inboxId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReputationId() {
        return reputationId;
    }

    public void setReputationId(int reputationId) {
        this.reputationId = reputationId;
    }

    public OrderData getOrderData() {
        return orderData;
    }

    public void setOrderData(OrderData orderData) {
        this.orderData = orderData;
    }

    public RevieweeData getRevieweeData() {
        return revieweeData;
    }

    public void setRevieweeData(RevieweeData revieweeData) {
        this.revieweeData = revieweeData;
    }

    public ReputationData getReputationData() {
        return reputationData;
    }

    public void setReputationData(ReputationData reputationData) {
        this.reputationData = reputationData;
    }

}
