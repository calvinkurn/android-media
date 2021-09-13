package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.*
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 8/14/17.
 */
class InboxReputationMapper @Inject constructor() : Func1<Response<TokopediaWsV4Response?>?, InboxReputationDomain> {

    override fun call(response: Response<TokopediaWsV4Response?>?): InboxReputationDomain {
        response?.let {
            return if (response.isSuccessful) {
                if ((!response.body()!!.isNullData
                            && response.body()!!.errorMessageJoined == "")
                    || !response.body()!!.isNullData && response.body()!!.errorMessages == null
                ) {
                    val data = response.body()!!.convertDataObj(
                        InboxReputationPojo::class.java
                    )
                    mappingToDomain(data)
                } else {
                    if (response.body()!!.errorMessages != null
                        && response.body()!!.errorMessages.isNotEmpty()
                    ) {
                        throw ErrorMessageException(
                            response.body()!!.errorMessageJoined
                        )
                    } else {
                        throw ErrorMessageException("")
                    }
                }
            } else {
                var messageError: String? = ""
                if (response.body() != null) {
                    messageError = response.body()!!.errorMessageJoined
                }
                if (!TextUtils.isEmpty(messageError)) {
                    throw ErrorMessageException(messageError)
                } else {
                    throw RuntimeException(response.code().toString())
                }
            }
        }
        return InboxReputationDomain()
    }

    private fun mappingToDomain(data: InboxReputationPojo): InboxReputationDomain {
        return InboxReputationDomain(
            mappingToListInboxReputation(data.inboxReputation),
            mappingToPaging(data.paging)
        )
    }

    private fun mappingToPaging(paging: Paging?): PagingDomain {
        return if (paging != null) PagingDomain(
            paging.isHasNext,
            paging.isHasPrev
        ) else PagingDomain(isHasNext = false, isHasPrev = false)
    }

    private fun mappingToListInboxReputation(inboxReputation: List<InboxReputation>): List<InboxReputationItemDomain> {
        return inboxReputation.map {
            InboxReputationItemDomain(
                it.inboxId,
                it.shopId,
                it.userId,
                it.reputationId,
                mappingToOrderData(it.orderData),
                mappingToRevieweeData(it.revieweeData),
                mappingToReputationData(it.reputationData)
            )
        }
    }

    private fun mappingToReputationData(reputationData: ReputationData): ReputationDataDomain {
        return ReputationDataDomain(
            reputationData.revieweeScore,
            reputationData.revieweeScoreStatus,
            reputationData.isShowRevieweeScore,
            reputationData.reviewerScore,
            reputationData.reviewerScoreStatus,
            reputationData.isIsEditable,
            reputationData.isIsInserted,
            reputationData.isIsLocked,
            reputationData.isIsAutoScored,
            reputationData.isIsCompleted,
            reputationData.isShowLockingDeadline,
            reputationData.lockingDeadlineDays,
            reputationData.isShowBookmark,
            reputationData.actionMessage
        )
    }

    private fun mappingToOrderData(orderData: OrderData): OrderDataDomain {
        return OrderDataDomain(
            orderData.invoiceRefNum,
            orderData.createTimeFmt,
            orderData.invoiceUrl
        )
    }

    private fun mappingToRevieweeData(revieweeData: RevieweeData): RevieweeDataDomain {
        return RevieweeDataDomain(
            revieweeData.revieweeName,
            revieweeData.revieweeUri,
            revieweeData.revieweeRole,
            revieweeData.revieweeRoleId,
            revieweeData.revieweePicture,
            mappingToRevieweeBadgeCustomer(revieweeData.revieweeBuyerBadge),
            mappingToRevieweeBadgeSeller(revieweeData.revieweeShopBadge)
        )
    }

    private fun mappingToRevieweeBadgeCustomer(revieweeBadge: RevieweeBuyerBadge): RevieweeBadgeCustomerDomain {
        return RevieweeBadgeCustomerDomain(
            revieweeBadge.positive,
            revieweeBadge.neutral,
            revieweeBadge.negative,
            revieweeBadge.positivePercentage,
            revieweeBadge.noReputation
        )
    }

    private fun mappingToRevieweeBadgeSeller(revieweeBadge: RevieweeShopBadge): RevieweeBadgeSellerDomain {
        return RevieweeBadgeSellerDomain(
            revieweeBadge.tooltip,
            revieweeBadge.reputationScore,
            revieweeBadge.score,
            revieweeBadge.minBadgeScore,
            revieweeBadge.reputationBadgeUrl,
            mappingToReputationBadge(revieweeBadge.reputationBadge)
        )
    }

    private fun mappingToReputationBadge(reputationBadge: ReputationBadge): ReputationBadgeDomain {
        return ReputationBadgeDomain(
            reputationBadge.level,
            reputationBadge.set
        )
    }
}