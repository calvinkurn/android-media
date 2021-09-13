package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.*
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailMapper : Func1<Response<TokopediaWsV4Response?>, ReviewDomain> {
    override fun call(response: Response<TokopediaWsV4Response?>): ReviewDomain {
        return if (response.isSuccessful) {
            if ((!response.body()!!.isNullData
                        && response.body()!!.errorMessageJoined == "")
                || !response.body()!!.isNullData && response.body()!!.errorMessages == null
            ) {
                val data = response.body()
                    .convertDataObj(InboxReputationDetailPojo::class.java)
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
                throw ErrorMessageException(
                    messageError
                )
            } else {
                throw RuntimeException(response.code().toString())
            }
        }
    }

    private fun mappingToDomain(data: InboxReputationDetailPojo): ReviewDomain {
        return ReviewDomain(
            convertToListReview(data),
            data.reputationId,
            convertToUserDataDomain(data.userData),
            convertToShopDataDomain(data.shopData),
            data.invoiceRefNum,
            data.invoiceTime,
            data.orderId
        )
    }

    private fun convertToShopDataDomain(shopData: ShopData?): ShopDataDomain {
        return ShopDataDomain(
            shopData.getShopId(),
            shopData.getShopUserId(),
            shopData.getDomain(),
            shopData.getShopName(),
            shopData.getShopUrl(),
            shopData.getLogo(),
            convertToShopReputationDomain(shopData.getShopReputation())
        )
    }

    private fun convertToShopReputationDomain(shopReputation: ShopReputation?): ShopReputationDomain {
        return ShopReputationDomain(
            shopReputation.getTooltip(),
            shopReputation.getReputationScore(),
            shopReputation.getScore(),
            shopReputation.getMinBadgeScore(),
            shopReputation.getReputationBadgeUrl(),
            convertToReputationBadgeDomain(shopReputation.getReputationBadge())
        )
    }

    private fun convertToReputationBadgeDomain(reputationBadge: ReputationBadge?): ReputationBadgeDomain {
        return ReputationBadgeDomain(
            reputationBadge.getLevel(),
            reputationBadge.getSet()
        )
    }

    private fun convertToUserDataDomain(userData: UserData?): UserDataDomain {
        return UserDataDomain(
            userData.getUserId(),
            userData.getFullName(),
            userData.getUserEmail(),
            userData.getUserStatus(),
            userData.getUserUrl(),
            userData.getUserLabel(),
            userData.getUserProfilePict(),
            convertToUserReputationDomain(userData.getUserReputation())
        )
    }

    private fun convertToUserReputationDomain(userReputation: UserReputation?): UserReputationDomain {
        return UserReputationDomain(
            userReputation.getPositive(),
            userReputation.getNeutral(),
            userReputation.getNegative(),
            userReputation.getPositivePercentage(),
            userReputation.getNoReputation()
        )
    }

    private fun convertToListReview(data: InboxReputationDetailPojo): List<ReviewItemDomain?> {
        val list: MutableList<ReviewItemDomain?> = ArrayList()
        for (pojo in data.reviewInboxData) {
            list.add(convertToReputationItem(pojo))
        }
        return list
    }

    private fun convertToReputationItem(pojo: ReviewInboxDatum?): ReviewItemDomain {
        return ReviewItemDomain(
            convertToProductDataDomain(pojo.getProductData()),
            pojo.getReviewInboxId(),
            pojo.getReviewId(),
            pojo!!.isReviewHasReviewed,
            pojo.isReviewIsSkippable,
            pojo.isReviewIsSkipped,
            pojo.isReviewIsEditable,
            convertToReviewDataDomain(pojo.reviewData)
        )
    }

    private fun convertToReviewDataDomain(reviewData: ReviewData?): ReviewDataDomain {
        return ReviewDataDomain(
            reviewData.getReviewId(),
            reviewData.getReputationId(),
            reviewData.getReviewTitle(),
            reviewData.getReviewMessage(),
            reviewData.getReviewRating(),
            convertToImageAttachmentDomain(reviewData.getReviewImageUrl()),
            convertToReviewCreateTime(reviewData.getReviewCreateTime()),
            convertToReviewUpdateTime(reviewData.getReviewUpdateTime()),
            reviewData!!.isReviewAnonymity,
            convertToReviewResponseDomain(reviewData.reviewResponse)
        )
    }

    private fun convertToReviewUpdateTime(reviewUpdateTime: ReviewUpdateTime?): ReviewUpdateTimeDomain {
        return ReviewUpdateTimeDomain(
            reviewUpdateTime.getDateTimeFmt1(),
            reviewUpdateTime.getUnixTimestamp(),
            reviewUpdateTime.getDateTimeIos(),
            reviewUpdateTime.getDateTimeAndroid()
        )
    }

    private fun convertToReviewCreateTime(reviewCreateTime: ReviewCreateTime?): ReviewCreateTimeDomain {
        return ReviewCreateTimeDomain(
            reviewCreateTime.getDateTimeFmt1(),
            reviewCreateTime.getUnixTimestamp(),
            reviewCreateTime.getDateTimeIos(),
            reviewCreateTime.getDateTimeAndroid()
        )
    }

    private fun convertToImageAttachmentDomain(reviewImageUrl: List<ReviewImageUrl?>?): List<ImageAttachmentDomain?> {
        val list: MutableList<ImageAttachmentDomain?> = ArrayList()
        for (pojo in reviewImageUrl!!) {
            list.add(
                ImageAttachmentDomain(
                    pojo.getAttachmentId(),
                    pojo.getDescription(),
                    pojo.getUriThumbnail(),
                    pojo.getUriLarge()
                )
            )
        }
        return list
    }

    private fun convertToReviewResponseDomain(reviewResponse: ReviewResponse?): ReviewResponseDomain {
        return ReviewResponseDomain(
            reviewResponse.getResponseMessage(),
            convertToResponseCreateTime(reviewResponse.getResponseCreateTime()),
            reviewResponse.getResponseBy()
        )
    }

    private fun convertToResponseCreateTime(responseCreateTime: ResponseCreateTime?): ResponseCreateTimeDomain {
        return ResponseCreateTimeDomain(
            responseCreateTime.getDateTimeFmt1(),
            responseCreateTime.getUnixTimestamp(),
            responseCreateTime.getDateTimeIos(),
            responseCreateTime.getDateTimeAndroid()
        )
    }

    private fun convertToProductDataDomain(productData: ProductData?): ProductDataDomain {
        return ProductDataDomain(
            productData.getProductId(),
            productData.getProductName(),
            productData.getProductImageUrl(),
            productData.getProductPageUrl(),
            productData.getShopId(),
            productData.getProductStatus()
        )
    }
}