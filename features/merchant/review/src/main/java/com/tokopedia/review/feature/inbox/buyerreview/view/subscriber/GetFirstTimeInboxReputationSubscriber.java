package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel;

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

    protected InboxReputationUiModel mappingToViewModel(InboxReputationDomain inboxReputationDomain) {
        return new InboxReputationUiModel
                (convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
                        inboxReputationDomain.getPaging().isHasNext()
                );
    }

    private List<InboxReputationItemUiModel> convertToInboxReputationList(List<InboxReputationItemDomain> inboxReputationDomain) {
        List<InboxReputationItemUiModel> list = new ArrayList<>();
        for (InboxReputationItemDomain domain : inboxReputationDomain) {
            list.add(new InboxReputationItemUiModel(
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

    private RevieweeBadgeSellerUiModel convertToSellerReputationViewModel(RevieweeBadgeSellerDomain revieweeBadgeSeller) {
        return new RevieweeBadgeSellerUiModel(revieweeBadgeSeller.getTooltip(),
                revieweeBadgeSeller.getReputationScore(),
                revieweeBadgeSeller.getScore(),
                revieweeBadgeSeller.getMinBadgeScore(),
                revieweeBadgeSeller.getReputationBadgeUrl(),
                convertToReputationBadgeViewModel(revieweeBadgeSeller.getReputationBadge()),
                revieweeBadgeSeller.getIsFavorited());
    }

    private ReputationBadgeUiModel convertToReputationBadgeViewModel(ReputationBadgeDomain reputationBadge) {
        return new ReputationBadgeUiModel(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    private RevieweeBadgeCustomerUiModel convertToBuyerReputationViewModel(
            RevieweeBadgeCustomerDomain revieweeBadgeCustomer) {
        return new RevieweeBadgeCustomerUiModel(revieweeBadgeCustomer.getPositive(),
                revieweeBadgeCustomer.getNeutral(), revieweeBadgeCustomer.getNegative(),
                revieweeBadgeCustomer.getPositivePercentage(),
                revieweeBadgeCustomer.getNoReputation());
    }

    private ReputationDataUiModel convertToReputationViewModel(ReputationDataDomain reputationData) {
        return new ReputationDataUiModel(reputationData.getRevieweeScore(),
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
