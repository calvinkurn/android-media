package com.tokopedia.content.product.preview.data.repository

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel

/**
 * @author by astidhiyaa on 06/12/23
 */
interface ProductPreviewRepository {
    suspend fun getProductMiniInfo(productId: String): BottomNavUiModel
    suspend fun getReview(productId: String, page: Int): List<ReviewUiModel>

    suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean

    suspend fun likeReview() //TODO: return LikeStatus, param [reviewId, LikeStatus]
    suspend fun submitReport(): Boolean //TODO: param [reviewId, ReportUiModel]

    suspend fun remindMe(productId: String) : BottomNavUiModel.RemindMeUiModel

}
