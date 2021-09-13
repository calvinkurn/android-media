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
open class GetFirstTimeInboxReputationSubscriber constructor(protected val viewListener: InboxReputation.View) :
    Subscriber<InboxReputationDomain>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoadingFull()
        viewListener.onErrorGetFirstTimeInboxReputation(e)
    }

    override fun onNext(inboxReputationDomain: InboxReputationDomain) {
        viewListener.finishLoadingFull()
        if (inboxReputationDomain.inboxReputation.isNullOrEmpty()) {
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
            convertToInboxReputationList(inboxReputationDomain.inboxReputation),
            inboxReputationDomain.paging.isHasNext
        )
    }

    private fun convertToInboxReputationList(inboxReputationDomain: List<InboxReputationItemDomain>): List<InboxReputationItemUiModel> {
        return inboxReputationDomain.map {
            InboxReputationItemUiModel(
                it.reputationId.toString(),
                it.revieweeData.revieweeName,
                it.orderData.createTimeFmt,
                it.revieweeData.revieweePicture,
                it.reputationData.lockingDeadlineDays.toString(),
                it.orderData.invoiceRefNum,
                convertToReputationViewModel(it.reputationData),
                it.revieweeData.revieweeRoleId,
                convertToBuyerReputationViewModel(
                    it.revieweeData
                        .revieweeBadgeCustomer
                ),
                convertToSellerReputationViewModel(
                    it.revieweeData
                        .revieweeBadgeSeller
                ),
                it.shopId,
                it.userId
            )
        }
    }

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: RevieweeBadgeSellerDomain?): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.Tooltip,
            revieweeBadgeSeller.ReputationScore,
            revieweeBadgeSeller.Score,
            revieweeBadgeSeller.MinBadgeScore,
            revieweeBadgeSeller.ReputationBadgeUrl,
            convertToReputationBadgeViewModel(revieweeBadgeSeller.ReputationBadge),
            revieweeBadgeSeller.IsFavorited
        )
    }

    private fun convertToReputationBadgeViewModel(reputationBadge: ReputationBadgeDomain?): ReputationBadgeUiModel {
        return ReputationBadgeUiModel(
            reputationBadge.Level,
            reputationBadge.Set
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: RevieweeBadgeCustomerDomain?
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.Positive,
            revieweeBadgeCustomer.Neutral, revieweeBadgeCustomer.Negative,
            revieweeBadgeCustomer.PositivePercentage,
            revieweeBadgeCustomer.NoReputation
        )
    }

    private fun convertToReputationViewModel(reputationData: ReputationDataDomain?): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.RevieweeScore,
            reputationData.RevieweeScoreStatus,
            reputationData!!.isShowRevieweeScore,
            reputationData.ReviewerScore,
            reputationData.ReviewerScoreStatus,
            reputationData!!.isEditable,
            reputationData!!.isInserted,
            reputationData!!.isLocked,
            reputationData!!.isAutoScored,
            reputationData!!.isCompleted,
            reputationData!!.isShowLockingDeadline,
            reputationData.LockingDeadlineDays,
            reputationData!!.isShowBookmark,
            reputationData.ActionMessage
        )
    }
}