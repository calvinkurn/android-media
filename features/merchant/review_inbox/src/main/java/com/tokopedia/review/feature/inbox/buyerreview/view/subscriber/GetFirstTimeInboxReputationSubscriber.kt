package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel
import rx.Subscriber

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

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: RevieweeBadgeSellerDomain): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.tooltip,
            revieweeBadgeSeller.reputationScore,
            revieweeBadgeSeller.score,
            revieweeBadgeSeller.minBadgeScore,
            revieweeBadgeSeller.reputationBadgeUrl,
            convertToReputationBadgeViewModel(revieweeBadgeSeller.reputationBadge),
            revieweeBadgeSeller.isFavorited
        )
    }

    private fun convertToReputationBadgeViewModel(reputationBadge: ReputationBadgeDomain): ReputationBadgeUiModel {
        return ReputationBadgeUiModel(
            reputationBadge.level,
            reputationBadge.set
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: RevieweeBadgeCustomerDomain
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.positive,
            revieweeBadgeCustomer.neutral, revieweeBadgeCustomer.negative,
            revieweeBadgeCustomer.positivePercentage,
            revieweeBadgeCustomer.noReputation
        )
    }

    private fun convertToReputationViewModel(reputationData: ReputationDataDomain): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.revieweeScore,
            reputationData.revieweeScoreStatus,
            reputationData.isShowRevieweeScore,
            reputationData.reviewerScore,
            reputationData.reviewerScoreStatus,
            reputationData.isEditable,
            reputationData.isInserted,
            reputationData.isLocked,
            reputationData.isAutoScored,
            reputationData.isCompleted,
            reputationData.isShowLockingDeadline,
            reputationData.lockingDeadlineDays,
            reputationData.isShowBookmark,
            reputationData.actionMessage
        )
    }
}