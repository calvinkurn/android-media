package com.tokopedia.shop.review.product.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

/**
 * Created by zulfikarrahman on 1/22/18.
 */
class ReviewProductAdapter<T : ReviewProductModel?, F : ReviewProductTypeFactoryAdapter?> : BaseListAdapter<T, F> {
    constructor(baseListAdapterTypeFactory: F) : super(baseListAdapterTypeFactory) {}

    fun updateLikeStatus(likeStatus: Int, totalLike: Int, reviewId: String?) {
        var i = 0
        val it: Iterator<T?> = data.iterator()
        while (it.hasNext()) {
            val productReviewModel: ReviewProductModel? = it.next()
            if (productReviewModel is ReviewProductModelContent) {
                val productReviewModelContent = productReviewModel
                if (productReviewModelContent.reviewId.equals(reviewId, ignoreCase = true)) {
                    productReviewModelContent.isLikeStatus = likeStatus == LIKE_STATUS_ACTIVE
                    productReviewModelContent.totalLike = totalLike
                    notifyItemChanged(i)
                    return
                }
            }
            i++
        }
    }

    fun updateDeleteReview(reviewId: String?) {
        var i = 0
        val it: Iterator<T?> = data.iterator()
        while (it.hasNext()) {
            val productReviewModel: ReviewProductModel? = it.next()
            if (productReviewModel is ReviewProductModelContent) {
                val productReviewModelContent = productReviewModel
                if (productReviewModelContent.reviewId.equals(reviewId, ignoreCase = true)) {
                    productReviewModelContent.isReviewHasReplied = false
                    notifyItemChanged(i)
                    return
                }
            }
            i++
        }
    }

    fun updateLikeStatusError(reviewId: String?, likeStatus: Int) {
        var i = 0
        val it: Iterator<T?> = data.iterator()
        while (it.hasNext()) {
            val productReviewModel: ReviewProductModel? = it.next()
            if (productReviewModel is ReviewProductModelContent) {
                val productReviewModelContent = productReviewModel
                if (productReviewModelContent.reviewId.equals(reviewId, ignoreCase = true)) {
                    productReviewModelContent.isLikeStatus = likeStatus != LIKE_STATUS_ACTIVE
                    productReviewModelContent.totalLike = (if (productReviewModelContent.totalLike > 0) productReviewModelContent.totalLike - 1 else 0)
                    notifyItemChanged(i)
                    return
                }
            }
            i++
        }
    }

    companion object {
        private const val LIKE_STATUS_ACTIVE = 1
    }
}