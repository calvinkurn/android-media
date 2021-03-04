package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemDomain {
    private int inboxId;
    private int shopId;
    private int userId;
    private int reputationId;
    private OrderDataDomain orderData;
    private RevieweeDataDomain revieweeData;
    private ReputationDataDomain reputationData;

    public InboxReputationItemDomain(int inboxId, int shopId, int userId,
                                     int reputationId, OrderDataDomain orderData,
                                     RevieweeDataDomain revieweeData,
                                     ReputationDataDomain reputationData) {
        this.inboxId = inboxId;
        this.shopId = shopId;
        this.userId = userId;
        this.reputationId = reputationId;
        this.orderData = orderData;
        this.revieweeData = revieweeData;
        this.reputationData = reputationData;
    }

    public int getInboxId() {
        return inboxId;
    }

    public int getShopId() {
        return shopId;
    }

    public int getUserId() {
        return userId;
    }

    public int getReputationId() {
        return reputationId;
    }

    public OrderDataDomain getOrderData() {
        return orderData;
    }

    public RevieweeDataDomain getRevieweeData() {
        return revieweeData;
    }

    public ReputationDataDomain getReputationData() {
        return reputationData;
    }
}
