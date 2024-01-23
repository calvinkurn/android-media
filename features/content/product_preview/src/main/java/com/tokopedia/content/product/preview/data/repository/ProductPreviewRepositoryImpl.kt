package com.tokopedia.content.product.preview.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewMapper
import com.tokopedia.content.product.preview.data.usecase.MediaReviewUseCase
import com.tokopedia.content.product.preview.data.usecase.ProductMiniInfoUseCase
import com.tokopedia.content.product.preview.data.usecase.RemindMeUseCase
import com.tokopedia.content.product.preview.data.usecase.ReviewLikeUseCase
import com.tokopedia.content.product.preview.data.usecase.SubmitReportUseCase
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.LikeUiState.LikeStatus.Companion.switch
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val miniInfoUseCase: ProductMiniInfoUseCase,
    private val getReviewUseCase: MediaReviewUseCase,
    private val likeUseCase: ReviewLikeUseCase,
    private val submitReportUseCase: SubmitReportUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val remindMeUseCase: RemindMeUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val mapper: ProductPreviewMapper
) : ProductPreviewRepository {

    override suspend fun getProductMiniInfo(productId: String): BottomNavUiModel =
        withContext(dispatchers.io) {
            val response = miniInfoUseCase(ProductMiniInfoUseCase.Param(productId))
            mapper.mapMiniInfo(response)
        }

    override suspend fun getReview(productId: String, page: Int): ReviewUiModel =
        withContext(dispatchers.io) {
            val response = getReviewUseCase(MediaReviewUseCase.Param(productId, page))
            mapper.mapReviews(response, page)
        }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean = withContext(dispatchers.io) {
        val response = addToCartUseCase.apply {
            setParams(
                AddToCartUseCase.getMinimumParams(
                    productId = productId,
                    shopId = shopId,
                    atcExternalSource = AtcFromExternalSource.ATC_FROM_PRODUCT_PREVIEW,
                    productName = productName,
                    price = price.toString(),
                    userId = userSessionInterface.userId
                )
            )
        }.executeOnBackground()
        !response.isStatusError()
    }

    override suspend fun likeReview(state: LikeUiState, reviewId: String): LikeUiState = withContext(dispatchers.io) {
        val response =
            likeUseCase(ReviewLikeUseCase.Param(reviewId = reviewId, likeStatus = state.state.switch.value))
        mapper.mapLike(response)
    }

    override suspend fun submitReport(report: ReportUiModel, reviewId: String): Boolean =
        withContext(dispatchers.io) {
            val response = submitReportUseCase(
                SubmitReportUseCase.Param(
                    reasonCode = report.reasonCode,
                    reasonText = report.text,
                    reviewId = reviewId.toIntOrZero()
                )
            )
            response.data.success
        }

    override suspend fun remindMe(productId: String): BottomNavUiModel.RemindMeUiModel =
        withContext(dispatchers.io) {
            val response = remindMeUseCase(
                RemindMeUseCase.Param(
                    userId = userSessionInterface.userId.toLongOrZero(),
                    productId = productId.toLongOrZero()
                )
            )
            mapper.mapRemindMe(response)
        }
}
