package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationItemViewModel implements Visitable<InboxReputationTypeFactory> {
    public static final int ROLE_SELLER = 2;
    public static final int ROLE_BUYER = 1;
    private final RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel;
    private final RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel;
    private final int shopId;
    private final int userId;
    private String revieweeName;
    private String createTime;
    private String revieweePicture;
    private String reputationDaysLeft;
    private String invoice;
    private String reputationId;
    private ReputationDataViewModel reputationDataViewModel;
    private int role;

    public InboxReputationItemViewModel(
            String reputationId, String revieweeName, String createTime,
            String revieweePicture, String reputationDaysLeft,
            String invoice,
            ReputationDataViewModel reputationDataViewModel,
            int role,
            RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
            RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel,
            int shopId, int userId) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.createTime = createTime;
        this.revieweePicture = revieweePicture;
        this.reputationDaysLeft = reputationDaysLeft;
        this.invoice = invoice;
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

    public ReputationDataViewModel getReputationDataViewModel() {
        return reputationDataViewModel;
    }

    public RevieweeBadgeCustomerViewModel getRevieweeBadgeCustomerViewModel() {
        return revieweeBadgeCustomerViewModel;
    }

    public RevieweeBadgeSellerViewModel getRevieweeBadgeSellerViewModel() {
        return revieweeBadgeSellerViewModel;
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
