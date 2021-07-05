package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.*

class ReviewProductViewModelTest : ReviewProductViewModelTestFixture() {

    @Test
    fun `when getRatingReview success should execute expected usecase`() {
        val productId = anyString()
        val expectedNetworkResponse = DataResponseReviewStarCount()
        coEvery { reviewProductGetRatingUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.getRatingReview(productId)
        coVerify { reviewProductGetRatingUseCase.executeOnBackground() }
        viewModel.getRatingReview().verifyValueEquals(expectedNetworkResponse)
    }

    @Test
    fun `when getRatingReview fail should not update value`() {
        val productId = anyString()
        coEvery { reviewProductGetRatingUseCase.executeOnBackground() } throws Throwable()
        viewModel.getRatingReview(productId)
        coVerify { reviewProductGetRatingUseCase.executeOnBackground() }
        viewModel.getRatingReview().verifyValueEquals(null)
    }

    @Test
    fun `when getHelpfulReview success should execute expected usecase`() {
        val productId = anyString()
        val expectedNetworkResponse = DataResponseReviewHelpful()
        val expectedResult = anyList<ReviewProductModel>()
        coEvery { reviewProductGetHelpfulUseCase.executeOnBackground() } returns expectedNetworkResponse
        coEvery { reviewProductListMapper.map(expectedNetworkResponse, anyString(), anyString()) } returns expectedResult
        viewModel.getHelpfulReview(productId)
        coVerify { reviewProductGetHelpfulUseCase.executeOnBackground() }
        viewModel.getHelpfulReviewList().verifyValueEquals(expectedResult)
    }

    @Test
    fun `when getHelpfulReview fail should not update value`() {
        coEvery { reviewProductGetHelpfulUseCase.executeOnBackground() } throws Throwable()
        viewModel.getHelpfulReview(anyString())
        coVerify { reviewProductGetHelpfulUseCase.executeOnBackground() }
        viewModel.getHelpfulReviewList().verifyValueEquals(null)
    }

    @Test
    fun `when getProductReview should success execute expected usecase`() {
        val productId = anyString()
        val page = anyInt()
        val rating = anyString()
        val isWithImage = anyBoolean()
        val expectedNetworkResponse = DataResponseReviewProduct()
        val userId = anyString()
        val reviewProductList = anyList<ReviewProductModel>()
        coEvery { reviewProductGetListUseCase.executeOnBackground() } returns expectedNetworkResponse
        coEvery { userSession.userId } returns userId
        coEvery { reviewProductListMapper.map(expectedNetworkResponse, userId) } returns reviewProductList
        viewModel.getProductReview(productId, page, rating, isWithImage)
        verifyReviewProductGetListCalled()
        viewModel.getReviewProductList().verifySuccessEquals(Success(reviewProductList to true))
    }

    @Test
    fun `when getProductReview fail expected error`() {
        val productId = anyString()
        val page = anyInt()
        val rating = anyString()
        val isWithImage = anyBoolean()
        val expectedError = Throwable()
        coEvery { reviewProductGetListUseCase.executeOnBackground() } throws expectedError
        viewModel.getProductReview(productId, page, rating, isWithImage)
        verifyReviewProductGetListCalled()
        viewModel.getReviewProductList().verifyErrorEquals(Fail(expectedError))
    }

    @Test
    fun `when deleteReview success should execute expected usecase`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        // 1 to assign isSuccess
        val expectedNetworkResponse = DeleteReviewResponseDomain(1)
        onDeleteReview_thenReturn(expectedNetworkResponse)
        viewModel.deleteReview(reviewId, reputationId, productId)
        verifyDeleteReviewResponseCalled()
        viewModel.getDeleteReview().verifySuccessEquals(Success(reviewId))
    }

    @Test
    fun `when deleteReview sucess but with isSuccess 0 should execute expected usecase and expected runtime Error`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        // 0 to assign isSuccess false
        val expectedNetworkResponse = DeleteReviewResponseDomain(0)
        onDeleteReview_thenReturn(expectedNetworkResponse)
        viewModel.deleteReview(reviewId, reputationId, productId)
        verifyDeleteReviewResponseCalled()
        viewModel.getDeleteReview().verifyErrorEquals(Fail(RuntimeException()))
        viewModel.getShowProgressDialog().verifyValueEquals(false)
    }

    @Test
    fun `when deleteReview fail expected error`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        val expectedNetworkResponse = Throwable()
        coEvery { deleteReviewResponseUseCase.executeOnBackground() } throws expectedNetworkResponse
        viewModel.deleteReview(reviewId, reputationId, productId)
        verifyDeleteReviewResponseCalled()
        viewModel.getDeleteReview().verifyErrorEquals(Fail(expectedNetworkResponse))
        viewModel.getShowProgressDialog().verifyValueEquals(false)
    }

    @Test
    fun `when postLikeDislikeReview success should execute expected usecase`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val productId = anyString()
        val expectedNetworkResponse = LikeDislikeDomain(
                anyInt(), anyInt(), anyInt()
        )
        coEvery { likeDislikeReviewUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.postLikeDislikeReview(reviewId, likeStatus, productId)
        verifyLikeDislikeReviewCalled()
        viewModel.getPostLikeDislike().verifyValueEquals(expectedNetworkResponse to reviewId)
        viewModel.getShowProgressDialog().verifyValueEquals(false)
    }

    @Test
    fun `when postLikeDislikeReview fail expected error`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val productId = anyString()
        val expectedNetworkResponse = Throwable()
        coEvery { likeDislikeReviewUseCase.executeOnBackground() } throws expectedNetworkResponse
        viewModel.postLikeDislikeReview(reviewId, likeStatus, productId)
        verifyLikeDislikeReviewCalled()
        viewModel.getErrorPostLikeDislike().verifyValueEquals(Triple(expectedNetworkResponse, reviewId, likeStatus))
        viewModel.getShowProgressDialog().verifyValueEquals(false)
    }

    private fun onDeleteReview_thenReturn(deleteResponse: DeleteReviewResponseDomain) {
        coEvery { deleteReviewResponseUseCase.executeOnBackground() } returns deleteResponse
    }

    private fun verifyReviewProductGetListCalled() {
        coVerify { reviewProductGetListUseCase.executeOnBackground() }
    }

    private fun verifyDeleteReviewResponseCalled() {
        coVerify { deleteReviewResponseUseCase.executeOnBackground() }
    }

    private fun verifyLikeDislikeReviewCalled() {
        coVerify { likeDislikeReviewUseCase.executeOnBackground() }
    }
}

