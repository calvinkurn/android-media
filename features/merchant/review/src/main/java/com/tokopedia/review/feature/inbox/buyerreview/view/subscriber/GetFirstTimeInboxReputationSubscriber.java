package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.ReputationDataViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail.ReputationBadgeViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/14/17.
 */

public class GetFirstTimeInboxReputationSubscriber extends Subscriber<InboxReputationDomain> {

    protected final InboxReputation.View viewListener;

    public GetFirstTimeInboxReputationSubscriber(InboxReputation.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingFull();
        viewListener.onErrorGetFirstTimeInboxReputation(e);
    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.finishLoadingFull();
        if (inboxReputationDomain.getInboxReputation().isEmpty()) {
            viewListener.onShowEmpty();
        } else {
            viewListener.onSuccessGetFirstTimeInboxReputation(mappingToViewModel(inboxReputationDomain));
        }
    }

    protected InboxReputationViewModel mappingToViewModel(InboxReputationDomain inboxReputationDomain) {
        return new InboxReputationViewModel
                (convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
                        inboxReputationDomain.getPaging().isHasNext()
                );
    }

    private List<InboxReputationItemViewModel> convertToInboxReputationList(List<InboxReputationItemDomain> inboxReputationDomain) {
        List<InboxReputationItemViewModel> list = new ArrayList<>();
        for (InboxReputationItemDomain domain : inboxReputationDomain) {
            list.add(new InboxReputationItemViewModel(
                    String.valueOf(domain.getReputationId()),
                    domain.getRevieweeData().getRevieweeName(),
                    domain.getOrderData().getCreateTimeFmt(),
                    domain.getRevieweeData().getRevieweePicture(),
                    String.valueOf(domain.getReputationData().getLockingDeadlineDays()),
                    domain.getOrderData().getInvoiceRefNum(),
                    convertToReputationViewModel(domain.getReputationData()),
                    domain.getRevieweeData().getRevieweeRoleId(),
                    convertToBuyerReputationViewModel(domain.getRevieweeData()
                            .getRevieweeBadgeCustomer()),
                    convertToSellerReputationViewModel(domain.getRevieweeData()
                            .getRevieweeBadgeSeller()),
                    domain.getShopId(),
                    domain.getUserId()));

        }
        return list;
    }

    private RevieweeBadgeSellerViewModel convertToSellerReputationViewModel(RevieweeBadgeSellerDomain revieweeBadgeSeller) {
        return new RevieweeBadgeSellerViewModel(revieweeBadgeSeller.getTooltip(),
                revieweeBadgeSeller.getReputationScore(),
                revieweeBadgeSeller.getScore(),
                revieweeBadgeSeller.getMinBadgeScore(),
                revieweeBadgeSeller.getReputationBadgeUrl(),
                convertToReputationBadgeViewModel(revieweeBadgeSeller.getReputationBadge()),
                revieweeBadgeSeller.getIsFavorited());
    }

    private ReputationBadgeViewModel convertToReputationBadgeViewModel(ReputationBadgeDomain reputationBadge) {
        return new ReputationBadgeViewModel(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    private RevieweeBadgeCustomerViewModel convertToBuyerReputationViewModel(
            RevieweeBadgeCustomerDomain revieweeBadgeCustomer) {
        return new RevieweeBadgeCustomerViewModel(revieweeBadgeCustomer.getPositive(),
                revieweeBadgeCustomer.getNeutral(), revieweeBadgeCustomer.getNegative(),
                revieweeBadgeCustomer.getPositivePercentage(),
                revieweeBadgeCustomer.getNoReputation());
    }

    private ReputationDataViewModel convertToReputationViewModel(ReputationDataDomain reputationData) {
        return new ReputationDataViewModel(reputationData.getRevieweeScore(),
                reputationData.getRevieweeScoreStatus(),
                reputationData.isShowRevieweeScore(),
                reputationData.getReviewerScore(),
                reputationData.getReviewerScoreStatus(),
                reputationData.isEditable(),
                reputationData.isInserted(),
                reputationData.isLocked(),
                reputationData.isAutoScored(),
                reputationData.isCompleted(),
                reputationData.isShowLockingDeadline(),
                reputationData.getLockingDeadlineDays(),
                reputationData.isShowBookmark(),
                reputationData.getActionMessage());
    }
}
