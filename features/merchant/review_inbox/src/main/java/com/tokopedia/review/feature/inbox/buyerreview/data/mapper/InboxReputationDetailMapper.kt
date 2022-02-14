package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response
import com.tokopedia.review.common.util.ReviewBuyerReviewMapperUtil
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.InboxReputationDetailPojo
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ProductData
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReputationBadge
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ResponseCreateTime
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewCreateTime
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewData
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewImageUrl
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewInboxDatum
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewResponse
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ReviewUpdateTime
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ShopData
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.ShopReputation
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.UserData
import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail.UserReputation
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ImageAttachmentDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ProductDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ResponseCreateTimeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewCreateTimeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewItemDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewUpdateTimeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.UserDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.UserReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailMapper @Inject constructor() :
    Func1<Response<TokopediaWsV4Response?>?, ReviewDomain> {

    override fun call(response: Response<TokopediaWsV4Response?>?): ReviewDomain {
        response?.let {
            if (response.isSuccessful) {
                if (ReviewBuyerReviewMapperUtil.isResponseValid(it)) {
                    val data =
                        response.body()?.convertDataObj(InboxReputationDetailPojo::class.java)
                            ?: InboxReputationDetailPojo()
                    return mappingToDomain(data)
                } else {
                    if (ReviewBuyerReviewMapperUtil.isErrorValid(response)) {
                        throw ErrorMessageException(response.body()?.errorMessageJoined)
                    } else {
                        throw ErrorMessageException("")
                    }
                }
            } else {
                val messageError = response.body()?.errorMessageJoined ?: ""
                if (!TextUtils.isEmpty(messageError)) {
                    throw ErrorMessageException(messageError)
                } else {
                    throw RuntimeException(response.code().toString())
                }
            }
        }
        return ReviewDomain()
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

    private fun convertToShopDataDomain(shopData: ShopData): ShopDataDomain {
        return ShopDataDomain(
            shopData.shopId,
            shopData.shopUserId,
            shopData.domain,
            shopData.shopName,
            shopData.shopUrl,
            shopData.logo,
            convertToShopReputationDomain(shopData.shopReputation)
        )
    }

    private fun convertToShopReputationDomain(shopReputation: ShopReputation): ShopReputationDomain {
        return ShopReputationDomain(
            shopReputation.tooltip,
            shopReputation.reputationScore,
            shopReputation.score,
            shopReputation.minBadgeScore,
            shopReputation.reputationBadgeUrl,
            convertToReputationBadgeDomain(shopReputation.reputationBadge)
        )
    }

    private fun convertToReputationBadgeDomain(reputationBadge: ReputationBadge): ReputationBadgeDomain {
        return ReputationBadgeDomain(
            reputationBadge.level,
            reputationBadge.set
        )
    }

    private fun convertToUserDataDomain(userData: UserData): UserDataDomain {
        return UserDataDomain(
            userData.userId,
            userData.fullName,
            userData.userEmail,
            userData.userStatus,
            userData.userUrl,
            userData.userLabel,
            userData.userProfilePict,
            convertToUserReputationDomain(userData.userReputation)
        )
    }

    private fun convertToUserReputationDomain(userReputation: UserReputation): UserReputationDomain {
        return UserReputationDomain(
            userReputation.positive,
            userReputation.neutral,
            userReputation.negative,
            userReputation.positivePercentage,
            userReputation.noReputation
        )
    }

    private fun convertToListReview(data: InboxReputationDetailPojo): List<ReviewItemDomain> {
        return data.reviewInboxData.map {
            convertToReputationItem(it)
        }
    }

    private fun convertToReputationItem(pojo: ReviewInboxDatum): ReviewItemDomain {
        return ReviewItemDomain(
            convertToProductDataDomain(pojo.productData),
            pojo.reviewInboxId,
            pojo.reviewId,
            pojo.isReviewHasReviewed,
            pojo.isReviewIsSkippable,
            pojo.isReviewIsSkipped,
            pojo.isReviewIsEditable,
            convertToReviewDataDomain(pojo.reviewData)
        )
    }

    private fun convertToReviewDataDomain(reviewData: ReviewData): ReviewDataDomain {
        return ReviewDataDomain(
            reviewData.reviewId,
            reviewData.reputationId,
            reviewData.reviewTitle,
            reviewData.reviewMessage,
            reviewData.reviewRating,
            convertToImageAttachmentDomain(reviewData.reviewImageUrl),
            convertToReviewCreateTime(reviewData.reviewCreateTime),
            convertToReviewUpdateTime(reviewData.reviewUpdateTime),
            reviewData.isReviewAnonymity,
            convertToReviewResponseDomain(reviewData.reviewResponse)
        )
    }

    private fun convertToReviewUpdateTime(reviewUpdateTime: ReviewUpdateTime): ReviewUpdateTimeDomain {
        return ReviewUpdateTimeDomain(
            reviewUpdateTime.dateTimeFmt1
        )
    }

    private fun convertToReviewCreateTime(reviewCreateTime: ReviewCreateTime): ReviewCreateTimeDomain {
        return ReviewCreateTimeDomain(
            reviewCreateTime.dateTimeFmt1
        )
    }

    private fun convertToImageAttachmentDomain(reviewImageUrl: List<ReviewImageUrl>): List<ImageAttachmentDomain> {
        return reviewImageUrl.map {
            ImageAttachmentDomain(
                it.attachmentId,
                it.description,
                it.uriThumbnail,
                it.uriLarge
            )
        }
    }

    private fun convertToReviewResponseDomain(reviewResponse: ReviewResponse): ReviewResponseDomain {
        return ReviewResponseDomain(
            reviewResponse.responseMessage,
            convertToResponseCreateTime(reviewResponse.responseCreateTime),
            reviewResponse.responseBy
        )
    }

    private fun convertToResponseCreateTime(responseCreateTime: ResponseCreateTime): ResponseCreateTimeDomain {
        return ResponseCreateTimeDomain(
            responseCreateTime.dateTimeFmt1
        )
    }

    private fun convertToProductDataDomain(productData: ProductData): ProductDataDomain {
        return ProductDataDomain(
            productData.productId,
            productData.productName,
            productData.productImageUrl,
            productData.productPageUrl,
            productData.shopId,
            productData.productStatus
        )
    }
}