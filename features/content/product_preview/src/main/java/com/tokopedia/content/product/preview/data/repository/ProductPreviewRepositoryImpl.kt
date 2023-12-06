package com.tokopedia.content.product.preview.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewMapper
import com.tokopedia.content.product.preview.data.usecase.MediaReviewUseCase
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
//    private val miniInfoUseCase: ProductMiniInfoUseCase,
    private val getReviewUseCase: MediaReviewUseCase,
//    private val likeUseCase: ReviewLikeUseCase,
//    private val submitReportUseCase: SubmitReportUseCase,
//    private val addToCartUseCase: AddToCartUseCase,
//    private val userSessionInterface: UserSessionInterface,
    private val mapper: ProductPreviewMapper,
) : ProductPreviewRepository {
    override suspend fun getProductMiniInfo(productId: String) {
        //TODO("Not yet implemented")
    }

    override suspend fun getReview(productId: String, page: Int): List<ReviewUiModel> =
        withContext(dispatchers.io) {
            val response = getReviewUseCase(MediaReviewUseCase.Param(productId, page))
            mapper.map(response)
        }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun likeReview() {
        //TODO("Not yet implemented")
    }

    override suspend fun submitReport(): Boolean {
        TODO("Not yet implemented")
    }
}
