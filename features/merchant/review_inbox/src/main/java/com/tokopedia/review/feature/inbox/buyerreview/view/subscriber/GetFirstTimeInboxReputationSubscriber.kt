package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel
import rx.Subscriber

/**
 * @author by nisie on 8/14/17.
 */
open class GetFirstTimeInboxReputationSubscriber constructor(protected val viewListener: InboxReputation.View) :
    Subscriber<InboxReputationResponseWrapper.Data.Response>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoadingFull()
        viewListener.onErrorGetFirstTimeInboxReputation(e)
    }

    override fun onNext(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response) {
        viewListener.finishLoadingFull()
        if (inboxReputationResponse.reputationList.isEmpty()) {
            viewListener.onShowEmpty()
        } else {
            viewListener.onSuccessGetFirstTimeInboxReputation(
                mappingToViewModel(inboxReputationResponse)
            )
        }
    }

    protected fun mappingToViewModel(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response): InboxReputationUiModel {
        return InboxReputationUiModel(
            convertToInboxReputationList(inboxReputationResponse.reputationList),
            inboxReputationResponse.hasNext
        )
    }

    private fun convertToInboxReputationList(reputationList: List<InboxReputationResponseWrapper.Data.Response.Reputation>): List<InboxReputationItemUiModel> {
        return reputationList.map {
            InboxReputationItemUiModel(
                it.reputationIdStr,
                it.revieweeData.name,
                it.orderData.createTimeFmt,
                it.revieweeData.picture,
                it.reputationData.lockingDeadlineDays.toString(),
                it.orderData.invoiceRefNum,
                convertToReputationViewModel(it.reputationData),
                it.revieweeData.roleId,
                convertToBuyerReputationViewModel(it.revieweeData.buyerBadge),
                convertToSellerReputationViewModel(it.revieweeData.shopBadge),
                it.shopIdStr,
                it.userIdStr
            )
        }
    }

    private fun convertToSellerReputationViewModel(buyerBadge: InboxReputationResponseWrapper.Data.Response.Reputation.RevieweeData.ShopBadge): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            buyerBadge.tooltip,
            buyerBadge.reputationScore,
            buyerBadge.score,
            buyerBadge.minBadgeScore,
            buyerBadge.reputationBadgeUrl,
            Int.ZERO
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: InboxReputationResponseWrapper.Data.Response.Reputation.RevieweeData.BuyerBadge
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.positive,
            revieweeBadgeCustomer.neutral, revieweeBadgeCustomer.negative,
            revieweeBadgeCustomer.positivePercentage,
            revieweeBadgeCustomer.noReputation
        )
    }

    private fun convertToReputationViewModel(reputationData: InboxReputationResponseWrapper.Data.Response.Reputation.ReputationData): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.revieweeScore,
            reputationData.revieweeScoreStatus,
            reputationData.showRevieweeScore,
            reputationData.reviewerScore,
            reputationData.reviewerScoreStatus,
            reputationData.isEditable,
            reputationData.isInserted,
            reputationData.isLocked,
            reputationData.isAutoScored,
            reputationData.isCompleted,
            reputationData.showLockingDeadline,
            reputationData.lockingDeadlineDays,
            reputationData.showBookmark,
            reputationData.actionMessage
        )
    }
}