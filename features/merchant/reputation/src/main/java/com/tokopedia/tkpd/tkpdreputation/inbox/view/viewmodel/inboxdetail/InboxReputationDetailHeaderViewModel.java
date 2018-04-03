package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewModel implements
        Visitable<InboxReputationDetailTypeFactory> {

    private final RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel;
    private final RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel;
    private final int shopId;
    private final int userId;
    String avatarImage;
    String name;
    String deadline;
    ReputationDataViewModel reputationDataViewModel;
    int role;

    public InboxReputationDetailHeaderViewModel(
            String avatarImage, String name, String deadline,
            ReputationDataViewModel reputationDataViewModel,
            int role,
            RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
            RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel,
            int shopId, int userId) {
        this.avatarImage = avatarImage;
        this.name = name;
        this.deadline = deadline;
        this.reputationDataViewModel = reputationDataViewModel;
        this.role = role;
        this.revieweeBadgeCustomerViewModel = revieweeBadgeCustomerViewModel;
        this.revieweeBadgeSellerViewModel = revieweeBadgeSellerViewModel;
        this.shopId = shopId;
        this.userId = userId;
    }

    public int getUserId() {
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

    public int getShopId() {
        return shopId;
    }

    public RevieweeBadgeCustomerViewModel getRevieweeBadgeCustomerViewModel() {
        return revieweeBadgeCustomerViewModel;
    }

    public RevieweeBadgeSellerViewModel getRevieweeBadgeSellerViewModel() {
        return revieweeBadgeSellerViewModel;
    }

    public ReputationDataViewModel getReputationDataViewModel() {
        return reputationDataViewModel;
    }



    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
