package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderUiModel implements
        Visitable<InboxReputationDetailTypeFactory> {

    private final RevieweeBadgeCustomerUiModel revieweeBadgeCustomerUiModel;
    private final RevieweeBadgeSellerUiModel revieweeBadgeSellerUiModel;
    private final long shopId;
    private final long userId;
    String avatarImage;
    String name;
    String deadline;
    ReputationDataUiModel reputationDataUiModel;
    int role;

    public InboxReputationDetailHeaderUiModel(
            String avatarImage, String name, String deadline,
            ReputationDataUiModel reputationDataUiModel,
            int role,
            RevieweeBadgeCustomerUiModel revieweeBadgeCustomerUiModel,
            RevieweeBadgeSellerUiModel revieweeBadgeSellerUiModel,
            long shopId, long userId) {
        this.avatarImage = avatarImage;
        this.name = name;
        this.deadline = deadline;
        this.reputationDataUiModel = reputationDataUiModel;
        this.role = role;
        this.revieweeBadgeCustomerUiModel = revieweeBadgeCustomerUiModel;
        this.revieweeBadgeSellerUiModel = revieweeBadgeSellerUiModel;
        this.shopId = shopId;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public String getName() {
        return name;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getRole() {
        return role;
    }

    public long getShopId() {
        return shopId;
    }

    public RevieweeBadgeCustomerUiModel getRevieweeBadgeCustomerUiModel() {
        return revieweeBadgeCustomerUiModel;
    }

    public RevieweeBadgeSellerUiModel getRevieweeBadgeSellerUiModel() {
        return revieweeBadgeSellerUiModel;
    }

    public ReputationDataUiModel getReputationDataUiModel() {
        return reputationDataUiModel;
    }



    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
