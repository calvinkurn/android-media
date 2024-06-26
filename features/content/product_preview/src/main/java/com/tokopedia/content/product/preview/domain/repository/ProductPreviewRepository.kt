package com.tokopedia.content.product.preview.domain.repository

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel

/**
 * @author by astidhiyaa on 06/12/23
 */
interface ProductPreviewRepository {
    suspend fun getProductMiniInfo(productId: String): BottomNavUiModel
    suspend fun getReviewByIds(ids: List<String>): ReviewUiModel
    suspend fun getReview(productId: String, page: Int): ReviewUiModel
    suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean
    suspend fun likeReview(state: ReviewLikeUiState, reviewId: String): ReviewLikeUiState
    suspend fun submitReport(report: ReviewReportUiModel, reviewId: String): Boolean
    suspend fun remindMe(productId: String): BottomNavUiModel.RemindMeUiModel
}
