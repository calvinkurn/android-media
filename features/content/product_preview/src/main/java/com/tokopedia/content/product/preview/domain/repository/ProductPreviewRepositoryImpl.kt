package com.tokopedia.content.product.preview.domain.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewMapper
import com.tokopedia.content.product.preview.domain.usecase.MediaReviewUseCase
import com.tokopedia.content.product.preview.domain.usecase.ProductMiniInfoUseCase
import com.tokopedia.content.product.preview.domain.usecase.RemindMeUseCase
import com.tokopedia.content.product.preview.domain.usecase.ReviewByIdsUseCase
import com.tokopedia.content.product.preview.domain.usecase.ReviewLikeUseCase
import com.tokopedia.content.product.preview.domain.usecase.SubmitReportUseCase
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState.ReviewLikeStatus.Companion.switch
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
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
    private val getReviewByIdsUseCase: ReviewByIdsUseCase,
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

    override suspend fun getReviewByIds(ids: List<String>): ReviewUiModel =
        withContext(dispatchers.io) {
            val response = getReviewByIdsUseCase(ReviewByIdsUseCase.Param(ids = ids))
            mapper.mapReviewsByIds(response)
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

    override suspend fun likeReview(state: ReviewLikeUiState, reviewId: String): ReviewLikeUiState =
        withContext(dispatchers.io) {
            val response =
                likeUseCase(
                    ReviewLikeUseCase.Param(
                        reviewId = reviewId,
                        likeStatus = state.state.switch.value
                    )
                )
            mapper.mapLike(response)
        }

    override suspend fun submitReport(report: ReviewReportUiModel, reviewId: String): Boolean =
        withContext(dispatchers.io) {
            val response = submitReportUseCase(
                SubmitReportUseCase.Param(
                    reasonCode = report.reasonCode,
                    reasonText = report.text,
                    reviewId = reviewId,
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
