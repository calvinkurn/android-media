package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.*
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel
import rx.Subscriber
import java.util.*

/**
 * @author by nisie on 8/14/17.
 */
open class GetFirstTimeInboxReputationSubscriber constructor(protected val viewListener: InboxReputation.View?) :
    Subscriber<InboxReputationDomain?>() {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoadingFull()
        viewListener.onErrorGetFirstTimeInboxReputation(e)
    }

    public override fun onNext(inboxReputationDomain: InboxReputationDomain) {
        viewListener!!.finishLoadingFull()
        if (inboxReputationDomain.getInboxReputation().isEmpty()) {
            viewListener.onShowEmpty()
        } else {
            viewListener.onSuccessGetFirstTimeInboxReputation(
                mappingToViewModel(
                    inboxReputationDomain
                )
            )
        }
    }

    protected fun mappingToViewModel(inboxReputationDomain: InboxReputationDomain): InboxReputationUiModel {
        return InboxReputationUiModel(
            convertToInboxReputationList(inboxReputationDomain.getInboxReputation()),
            inboxReputationDomain.getPaging().isHasNext()
        )
    }

    private fun convertToInboxReputationList(inboxReputationDomain: List<InboxReputationItemDomain?>?): List<InboxReputationItemUiModel?> {
        val list: MutableList<InboxReputationItemUiModel?> = ArrayList()
        for (domain: InboxReputationItemDomain? in inboxReputationDomain!!) {
            list.add(
                InboxReputationItemUiModel(
                    domain.getReputationId().toString(),
                    domain.getRevieweeData().getRevieweeName(),
                    domain.getOrderData().getCreateTimeFmt(),
                    domain.getRevieweeData().getRevieweePicture(),
                    domain.getReputationData().getLockingDeadlineDays().toString(),
                    domain.getOrderData().getInvoiceRefNum(),
                    convertToReputationViewModel(domain.getReputationData()),
                    domain.getRevieweeData().getRevieweeRoleId(),
                    convertToBuyerReputationViewModel(
                        domain.getRevieweeData()
                            .getRevieweeBadgeCustomer()
                    ),
                    convertToSellerReputationViewModel(
                        domain.getRevieweeData()
                            .getRevieweeBadgeSeller()
                    ),
                    domain.getShopId(),
                    domain.getUserId()
                )
            )
        }
        return list
    }

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: RevieweeBadgeSellerDomain?): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.getTooltip(),
            revieweeBadgeSeller.getReputationScore(),
            revieweeBadgeSeller.getScore(),
            revieweeBadgeSeller.getMinBadgeScore(),
            revieweeBadgeSeller.getReputationBadgeUrl(),
            convertToReputationBadgeViewModel(revieweeBadgeSeller.getReputationBadge()),
            revieweeBadgeSeller.getIsFavorited()
        )
    }

    private fun convertToReputationBadgeViewModel(reputationBadge: ReputationBadgeDomain?): ReputationBadgeUiModel {
        return ReputationBadgeUiModel(
            reputationBadge.getLevel(),
            reputationBadge.getSet()
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: RevieweeBadgeCustomerDomain?
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.getPositive(),
            revieweeBadgeCustomer.getNeutral(), revieweeBadgeCustomer.getNegative(),
            revieweeBadgeCustomer.getPositivePercentage(),
            revieweeBadgeCustomer.getNoReputation()
        )
    }

    private fun convertToReputationViewModel(reputationData: ReputationDataDomain?): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.getRevieweeScore(),
            reputationData.getRevieweeScoreStatus(),
            reputationData!!.isShowRevieweeScore(),
            reputationData.getReviewerScore(),
            reputationData.getReviewerScoreStatus(),
            reputationData!!.isEditable(),
            reputationData!!.isInserted(),
            reputationData!!.isLocked(),
            reputationData!!.isAutoScored(),
            reputationData!!.isCompleted(),
            reputationData!!.isShowLockingDeadline(),
            reputationData.getLockingDeadlineDays(),
            reputationData!!.isShowBookmark(),
            reputationData.getActionMessage()
        )
    }
}