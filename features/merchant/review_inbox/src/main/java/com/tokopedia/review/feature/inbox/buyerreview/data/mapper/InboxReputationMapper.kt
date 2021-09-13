package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.*
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * @author by nisie on 8/14/17.
 */
class InboxReputationMapper : Func1<Response<TokopediaWsV4Response?>, InboxReputationDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>): InboxReputationDomain {
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
                    && !response.body()!!.errorMessages.isEmpty()
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
        ) else PagingDomain(false, false)
    }

    private fun mappingToListInboxReputation(inboxReputation: List<InboxReputation?>?): List<InboxReputationItemDomain?> {
        val list: MutableList<InboxReputationItemDomain?> = ArrayList()
        if (!inboxReputation!!.isEmpty()) {
            for (item in inboxReputation) {
                list.add(
                    InboxReputationItemDomain(
                        item.getInboxId(),
                        item.getShopId(),
                        item.getUserId(),
                        item.getReputationId(),
                        mappingToOrderData(item.getOrderData()),
                        mappingToRevieweeData(item.getRevieweeData()),
                        mappingToReputationData(item.getReputationData())
                    )
                )
            }
        }
        return list
    }

    private fun mappingToReputationData(reputationData: ReputationData?): ReputationDataDomain {
        return ReputationDataDomain(
            reputationData.getRevieweeScore(),
            reputationData.getRevieweeScoreStatus(),
            reputationData!!.isShowRevieweeScore,
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

    private fun mappingToOrderData(orderData: OrderData?): OrderDataDomain {
        return OrderDataDomain(
            orderData.getInvoiceRefNum(),
            orderData.getCreateTimeFmt(),
            orderData.getInvoiceUrl()
        )
    }

    private fun mappingToRevieweeData(revieweeData: RevieweeData?): RevieweeDataDomain {
        return RevieweeDataDomain(
            revieweeData.getRevieweeName(),
            revieweeData.getRevieweeUri(),
            revieweeData.getRevieweeRole(),
            revieweeData.getRevieweeRoleId(),
            revieweeData.getRevieweePicture(),
            mappingToRevieweeBadgeCustomer(revieweeData.getRevieweeBuyerBadge()),
            mappingToRevieweeBadgeSeller(revieweeData.getRevieweeShopBadge())
        )
    }

    private fun mappingToRevieweeBadgeCustomer(revieweeBadge: RevieweeBuyerBadge?): RevieweeBadgeCustomerDomain {
        return RevieweeBadgeCustomerDomain(
            revieweeBadge.getPositive(),
            revieweeBadge.getNeutral(),
            revieweeBadge.getNegative(),
            revieweeBadge.getPositivePercentage(),
            revieweeBadge.getNoReputation()
        )
    }

    private fun mappingToRevieweeBadgeSeller(revieweeBadge: RevieweeShopBadge?): RevieweeBadgeSellerDomain {
        return RevieweeBadgeSellerDomain(
            revieweeBadge.getTooltip(),
            revieweeBadge.getReputationScore(),
            revieweeBadge.getScore(),
            revieweeBadge.getMinBadgeScore(),
            revieweeBadge.getReputationBadgeUrl(),
            mappingToReputationBadge(revieweeBadge.getReputationBadge())
        )
    }

    private fun mappingToReputationBadge(reputationBadge: ReputationBadge?): ReputationBadgeDomain {
        return ReputationBadgeDomain(
            reputationBadge.getLevel(),
            reputationBadge.getSet()
        )
    }
}