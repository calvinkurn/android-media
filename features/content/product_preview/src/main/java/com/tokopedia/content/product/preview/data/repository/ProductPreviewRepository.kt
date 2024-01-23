package com.tokopedia.content.product.preview.data.repository

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel

/**
 * @author by astidhiyaa on 06/12/23
 */
interface ProductPreviewRepository {
    suspend fun getProductMiniInfo(productId: String): BottomNavUiModel
    suspend fun getReview(productId: String, page: Int): ReviewUiModel
    suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean
    suspend fun likeReview(state: LikeUiState, reviewId: String): LikeUiState
    suspend fun submitReport(report: ReportUiModel, reviewId: String): Boolean
    suspend fun remindMe(productId: String): BottomNavUiModel.RemindMeUiModel
}
