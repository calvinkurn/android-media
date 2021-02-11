package com.tokopedia.shop.review.product.view

import android.text.TextUtils
import com.tokopedia.shop.review.product.data.model.reviewlist.*
import com.tokopedia.shop.review.product.view.adapter.ReviewProductModel
import com.tokopedia.shop.review.product.view.adapter.ReviewProductModelContent
import com.tokopedia.shop.review.shop.view.adapter.ReviewShopModelContent
import com.tokopedia.shop.review.shop.view.uimodel.ImageAttachmentUiModel
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 1/17/18.
 */
class ReviewProductListMapper @Inject constructor() {
    fun map(dataResponseReviewProduct: DataResponseReviewProduct, userId: String): List<ReviewProductModel> {
        val reviewModelContents: MutableList<ReviewProductModel> = ArrayList()
        for (review in dataResponseReviewProduct.list!!) {
            val productReviewModelContent = ReviewProductModelContent()
            productReviewModelContent.responseCreateTime = review.reviewResponse!!.responseTime!!.dateTimeFmt1
            productReviewModelContent.responseMessage = review.reviewResponse!!.responseMessage
            productReviewModelContent.isReviewCanReported = isReviewCanReported(userId, review)
            productReviewModelContent.reputationId = java.lang.String.valueOf(review.reputationId)
            productReviewModelContent.productId = java.lang.String.valueOf(dataResponseReviewProduct.product!!.productId)
            productReviewModelContent.reviewerId = java.lang.String.valueOf(review.user!!.userId)
            productReviewModelContent.reviewAttachment = generateImageAttachmentModel(review)
            productReviewModelContent.isReviewIsAnonymous = review.reviewAnonymous == 1
            productReviewModelContent.reviewStar = review.productRating.toFloat()
            productReviewModelContent.sellerName = dataResponseReviewProduct.getOwner()!!.shop!!.shopName
            productReviewModelContent.reviewTime = getReviewCreateTime(review)
            productReviewModelContent.reviewMessage = review.reviewMessage
            productReviewModelContent.reviewerName = review.user!!.fullName
            productReviewModelContent.isReviewHasReplied = !TextUtils.isEmpty(review.reviewResponse!!.responseMessage)
            productReviewModelContent.isSellerRepliedOwner = isUserOwnedReplied(userId, dataResponseReviewProduct.getOwner())
            productReviewModelContent.shopId = java.lang.String.valueOf(dataResponseReviewProduct.getOwner()!!.shop!!.shopId)
            productReviewModelContent.isLikeStatus = review.likeStatus == LIKE_STATUS_ACTIVE
            productReviewModelContent.totalLike = review.totalLike
            productReviewModelContent.isLogin = !TextUtils.isEmpty(userId)
            productReviewModelContent.reviewId = java.lang.String.valueOf(review.reviewId)
            reviewModelContents.add(productReviewModelContent)
        }
        return reviewModelContents
    }

    fun map(dataResponseReviewHelpful: DataResponseReviewHelpful, userId: String, productId: String?): List<ReviewProductModel> {
        val reviewModelContents: MutableList<ReviewProductModel> = ArrayList()
        for (review in dataResponseReviewHelpful.list!!) {
            val productReviewModelContent = ReviewProductModelContent()
            productReviewModelContent.responseCreateTime = review.reviewResponse!!.responseTime!!.dateTimeFmt1
            productReviewModelContent.responseMessage = review.reviewResponse!!.responseMessage
            productReviewModelContent.isReviewCanReported = isReviewCanReported(userId, review)
            productReviewModelContent.reputationId = java.lang.String.valueOf(review.reputationId)
            productReviewModelContent.reviewerId = java.lang.String.valueOf(review.user!!.userId)
            productReviewModelContent.reviewAttachment = generateImageAttachmentModel(review)
            productReviewModelContent.isReviewIsAnonymous = review.reviewAnonymous == 1
            productReviewModelContent.reviewStar = review.reviewStar
            productReviewModelContent.sellerName = dataResponseReviewHelpful.getOwner()!!.shop!!.shopName
            productReviewModelContent.reviewTime = getReviewCreateTime(review)
            productReviewModelContent.reviewMessage = review.reviewMessage
            productReviewModelContent.reviewerName = review.user!!.fullName
            productReviewModelContent.isReviewHasReplied = !TextUtils.isEmpty(review.reviewResponse!!.responseMessage)
            productReviewModelContent.isSellerRepliedOwner = isUserOwnedReplied(userId, dataResponseReviewHelpful.getOwner())
            productReviewModelContent.shopId = java.lang.String.valueOf(dataResponseReviewHelpful.getOwner()!!.shop!!.shopId)
            productReviewModelContent.isLikeStatus = review.likeStatus == LIKE_STATUS_ACTIVE
            productReviewModelContent.totalLike = review.totalLike
            productReviewModelContent.reviewId = java.lang.String.valueOf(review.reviewId)
            productReviewModelContent.isLogin = !TextUtils.isEmpty(userId)
            productReviewModelContent.productId = productId
            productReviewModelContent.isHelpful = true
            reviewModelContents.add(productReviewModelContent)
        }
        return reviewModelContents
    }

    private fun isUserOwnedReplied(userId: String, owner: Owner?): Boolean {
        return userId == java.lang.String.valueOf(owner!!.user!!.userId)
    }

    private fun generateImageAttachmentModel(review: Review): List<ImageAttachmentUiModel?> {
        val imageAttachmentViewModels: MutableList<ImageAttachmentUiModel?> = ArrayList()
        for (reviewImageAttachment in review.reviewImageAttachment!!) {
            val imageAttachmentViewModel = ImageAttachmentUiModel(reviewImageAttachment.attachmentId,
                    reviewImageAttachment.description, reviewImageAttachment.uriThumbnail, reviewImageAttachment.uriLarge)
            imageAttachmentViewModels.add(imageAttachmentViewModel)
        }
        return imageAttachmentViewModels
    }

    private fun isReviewCanReported(userId: String, review: Review): Boolean {
        return !TextUtils.isEmpty(userId) && userId != java.lang.String.valueOf(review.user!!.userId)
    }

    private fun getReviewCreateTime(review: Review): String? {
        return if (!TextUtils.isEmpty(review.reviewUpdateTime!!.dateTimeFmt1)) {
            review.reviewUpdateTime!!.dateTimeFmt1
        } else {
            review.reviewCreateTime!!.dateTimeFmt1
        }
    }

    fun map(dataResponseReviewShop: DataResponseReviewShop, userId: String): List<ReviewShopModelContent> {
        val shopReviewModelContents: MutableList<ReviewShopModelContent> = ArrayList()
        for (review in dataResponseReviewShop.list!!) {
            val shopReviewModelContent = ReviewShopModelContent()
            shopReviewModelContent.responseCreateTime = review.reviewResponse!!.responseTime!!.dateTimeFmt1
            shopReviewModelContent.responseMessage = review.reviewResponse!!.responseMessage
            shopReviewModelContent.isReviewCanReported = isReviewCanReported(userId, review)
            shopReviewModelContent.reputationId = java.lang.String.valueOf(review.reputationId)
            shopReviewModelContent.productId = java.lang.String.valueOf(review.product!!.productId)
            shopReviewModelContent.reviewerId = java.lang.String.valueOf(review.user!!.userId)
            shopReviewModelContent.reviewAttachment = generateImageAttachmentModel(review)
            shopReviewModelContent.isReviewIsAnonymous = review.reviewAnonymous == 1
            shopReviewModelContent.reviewStar = review.productRating.toFloat()
            shopReviewModelContent.sellerName = dataResponseReviewShop.getOwner()!!.shop!!.shopName
            shopReviewModelContent.reviewTime = getReviewCreateTime(review)
            shopReviewModelContent.reviewMessage = review.reviewMessage
            shopReviewModelContent.reviewerName = review.user!!.fullName
            shopReviewModelContent.isReviewHasReplied = !TextUtils.isEmpty(review.reviewResponse!!.responseMessage)
            shopReviewModelContent.isSellerRepliedOwner = isUserOwnedReplied(userId, dataResponseReviewShop.getOwner())
            shopReviewModelContent.shopId = java.lang.String.valueOf(dataResponseReviewShop.getOwner()!!.shop!!.shopId)
            shopReviewModelContent.isLikeStatus = review.likeStatus == LIKE_STATUS_ACTIVE
            shopReviewModelContent.totalLike = review.totalLike
            shopReviewModelContent.isLogin = !TextUtils.isEmpty(userId)
            shopReviewModelContent.reviewId = java.lang.String.valueOf(review.reviewId)
            shopReviewModelContent.productName = review.product!!.productName
            shopReviewModelContent.productImageUrl = review.product!!.productImageUrl
            shopReviewModelContent.productPageUrl = review.product!!.productPageUrl
            shopReviewModelContents.add(shopReviewModelContent)
        }
        return shopReviewModelContents
    }

    companion object {
        const val LIKE_STATUS_ACTIVE = 1
    }
}