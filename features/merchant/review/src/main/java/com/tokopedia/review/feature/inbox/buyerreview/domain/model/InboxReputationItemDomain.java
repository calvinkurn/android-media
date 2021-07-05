package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemDomain {
    private long inboxId;
    private long shopId;
    private long userId;
    private long reputationId;
    private OrderDataDomain orderData;
    private RevieweeDataDomain revieweeData;
    private ReputationDataDomain reputationData;

    public InboxReputationItemDomain(long inboxId, long shopId, long userId,
                                     long reputationId, OrderDataDomain orderData,
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

    public long getInboxId() {
        return inboxId;
    }

    public long getShopId() {
        return shopId;
    }

    public long getUserId() {
        return userId;
    }

    public long getReputationId() {
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
