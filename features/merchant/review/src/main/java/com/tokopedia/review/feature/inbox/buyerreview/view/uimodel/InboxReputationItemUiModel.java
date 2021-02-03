package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemUiModel implements Visitable<InboxReputationTypeFactory> {
    public static final int ROLE_SELLER = 2;
    public static final int ROLE_BUYER = 1;
    private final RevieweeBadgeCustomerUiModel revieweeBadgeCustomerUiModel;
    private final RevieweeBadgeSellerUiModel revieweeBadgeSellerUiModel;
    private final int shopId;
    private final int userId;
    private String revieweeName;
    private String createTime;
    private String revieweePicture;
    private String reputationDaysLeft;
    private String invoice;
    private String reputationId;
    private ReputationDataUiModel reputationDataUiModel;
    private int role;

    public InboxReputationItemUiModel(
            String reputationId, String revieweeName, String createTime,
            String revieweePicture, String reputationDaysLeft,
            String invoice,
            ReputationDataUiModel reputationDataUiModel,
            int role,
            RevieweeBadgeCustomerUiModel revieweeBadgeCustomerUiModel,
            RevieweeBadgeSellerUiModel revieweeBadgeSellerUiModel,
            int shopId, int userId) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.createTime = createTime;
        this.revieweePicture = revieweePicture;
        this.reputationDaysLeft = reputationDaysLeft;
        this.invoice = invoice;
        this.reputationDataUiModel = reputationDataUiModel;
        this.role = role;
        this.revieweeBadgeCustomerUiModel = revieweeBadgeCustomerUiModel;
        this.revieweeBadgeSellerUiModel = revieweeBadgeSellerUiModel;
        this.shopId = shopId;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }


    public String getReputationDaysLeft() {
        return reputationDaysLeft;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getReputationId() {
        return reputationId;
    }

    public ReputationDataUiModel getReputationDataUiModel() {
        return reputationDataUiModel;
    }

    public RevieweeBadgeCustomerUiModel getRevieweeBadgeCustomerUiModel() {
        return revieweeBadgeCustomerUiModel;
    }

    public RevieweeBadgeSellerUiModel getRevieweeBadgeSellerUiModel() {
        return revieweeBadgeSellerUiModel;
    }

    public int getShopId() {
        return shopId;
    }

    @Override
    public int type(InboxReputationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getRole() {
        return role;
    }
}
