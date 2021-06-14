
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxReputation {

    @SerializedName("inbox_id")
    @Expose
    private long inboxId;
    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("reputation_id")
    @Expose
    private long reputationId;
    @SerializedName("order_data")
    @Expose
    private OrderData orderData;
    @SerializedName("reviewee_data")
    @Expose
    private RevieweeData revieweeData;
    @SerializedName("reputation_data")
    @Expose
    private ReputationData reputationData;

    public long getInboxId() {
        return inboxId;
    }

    public void setInboxId(long inboxId) {
        this.inboxId = inboxId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReputationId() {
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
