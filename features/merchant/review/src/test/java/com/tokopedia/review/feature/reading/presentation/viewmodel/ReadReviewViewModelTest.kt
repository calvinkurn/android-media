package com.tokopedia.review.feature.reading.presentation.viewmodel

import com.tokopedia.review.feature.reading.data.ProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductReviewList
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class ReadReviewViewModelTest : ReadReviewViewModelTestFixture() {

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected results`() {
        val productId = anyString()
        val expectedResponse = ProductRatingAndTopic()

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedResponse.productrevGetProductRatingAndTopics))
    }

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected error`() {
        val productId = anyString()
        val expectedResponse = Throwable()

        onGetProductRatingAndTopicsFail_thenReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when setPage should call getProductReviews and return expected results`() {
        val page = anyInt()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setPage(page)

        verifyGetProductReviewListUseCase()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setPage should call getProductReviews and return expected error`() {
        val page = anyInt()
        val expectedResponse = Throwable()

        onGetProductReviewsFail_thenReturn(expectedResponse)

        viewModel.setPage(page)

        verifyGetProductReviewListUseCase()
        verifyProductReviewsErrorEquals(Fail(expectedResponse))
    }

    private fun onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse: ProductRatingAndTopic) {
        coEvery { getProductRatingAndTopicsUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductRatingAndTopicsFail_thenReturn(throwable: Throwable) {
        coEvery { getProductRatingAndTopicsUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetProductReviewsSuccess_thenReturn(expectedResponse: ProductReviewList) {
        coEvery { getProductReviewListUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductReviewsFail_thenReturn(throwable: Throwable) {
        coEvery { getProductReviewListUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetProductRatingAndTopicsUseCaseExecuted() {
        coVerify { getProductRatingAndTopicsUseCase.executeOnBackground() }
    }

    private fun verifyGetProductReviewListUseCase() {
        coVerify { getProductReviewListUseCase.executeOnBackground() }
    }

    private fun verifyRatingAndTopicSuccessEquals(expectedSuccessValue: Success<ProductrevGetProductRatingAndTopic>) {
        viewModel.ratingAndTopic.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyRatingAndTopicErrorEquals(expectedErrorValue: Fail) {
        viewModel.ratingAndTopic.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyProductReviewsSuccessEquals(expectedSuccessValue: Success<ProductrevGetProductReviewList>) {
        viewModel.productReviews.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyProductReviewsErrorEquals(expectedErrorValue: Fail) {
        viewModel.productReviews.verifyErrorEquals(expectedErrorValue)
    }
}