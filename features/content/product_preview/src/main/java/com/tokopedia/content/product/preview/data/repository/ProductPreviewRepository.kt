package com.tokopedia.content.product.preview.data.repository

/**
 * @author by astidhiyaa on 06/12/23
 */
interface ProductPreviewRepository {
    suspend fun getProductMiniInfo(productId: String) //TODO: return MediaBottomNavUiModel
    suspend fun getReview(productId: String, page: Int) //TODO: return UiModel
    suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ) : Boolean

    suspend fun likeReview() //TODO: return LikeStatus, param [reviewId, LikeStatus]
    suspend fun submitReport(): Boolean //TODO: param [reviewId, ReportUiModel]

}
