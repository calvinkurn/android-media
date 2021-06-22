package com.tokopedia.review.feature.reading.presentation.viewmodel

import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.review.feature.reading.data.*
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class ReadReviewViewModelTest : ReadReviewViewModelTestFixture() {

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected results`() {
        val productId = anyString()
        val expectedResponse = ProductRatingAndTopic(ProductrevGetProductRatingAndTopic(
                ProductRating(satisfactionRate = "90% pembeli merasa puas",
                        detail = listOf(
                                ProductReviewDetail(5, 70, "70%"),
                                ProductReviewDetail(4, 10, "10%"),
                                ProductReviewDetail(3, 10, "10%"),
                                ProductReviewDetail(2, 10, "10%"),
                                ProductReviewDetail(1, 0, "0%"),
                        ))))
        val expectedSatisfactionRate = expectedResponse.productrevGetProductRatingAndTopics.rating.satisfactionRate
        val expectedStatistics = expectedResponse.productrevGetProductRatingAndTopics.rating.detail

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        val actualSatisfactionRate = viewModel.getReviewSatisfactionRate()
        val actualStatistics = viewModel.getReviewStatistics()

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedResponse.productrevGetProductRatingAndTopics))
        Assert.assertEquals(expectedSatisfactionRate, actualSatisfactionRate)
        Assert.assertEquals(expectedStatistics, actualStatistics)
    }

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected error`() {
        val productId = anyString()
        val expectedResponse = Throwable()
        val expectedSatisfactionRate = ""
        val expectedStatistics = listOf<String>()

        onGetProductRatingAndTopicsFail_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        val actualSatisfactionRate = viewModel.getReviewSatisfactionRate()
        val actualStatistics = viewModel.getReviewStatistics()

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicErrorEquals(Fail(expectedResponse))
        Assert.assertEquals(expectedSatisfactionRate, actualSatisfactionRate)
        Assert.assertEquals(expectedStatistics, actualStatistics)
    }

    @Test
    fun `when setPage should call getProductReviews and return expected results`() {
        val page = anyInt()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setPage(page)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setPage should call getProductReviews and return expected error`() {
        val page = anyInt()
        val expectedResponse = Throwable()

        onGetProductReviewsFail_thenReturn(expectedResponse)

        viewModel.setPage(page)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when mapProductReviewToReadReviewUiModel should return expected list of ReadReviewUiModel`() {
        val productReviews = listOf(ProductReview(feedbackID = "1"), ProductReview(feedbackID = "2"), ProductReview(feedbackID = "3"), ProductReview(feedbackID = "4"), ProductReview(feedbackID = "5"))
        val shopId = "mockShopId"
        val shopName = "My Shop"
        val expectedReadReviewUiModelList = listOf(
                ReadReviewUiModel(ProductReview("1"), false, shopId, shopName),
                ReadReviewUiModel(ProductReview("2"), false, shopId, shopName),
                ReadReviewUiModel(ProductReview("3"), false, shopId, shopName),
                ReadReviewUiModel(ProductReview("4"), false, shopId, shopName),
                ReadReviewUiModel(ProductReview("5"), false, shopId, shopName)
        )

        val result = viewModel.mapProductReviewToReadReviewUiModel(productReviews, shopId, shopName)

        Assert.assertEquals(expectedReadReviewUiModelList, result)
    }

    @Test
    fun `when setSort should set expected sort and make network call`() {
        val sort = SortTypeConstants.MOST_HELPFUL_PARAM
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setSort(sort)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setFilterWithImage should set expected filter and make network call`() {
        val isActive = false
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected results`() {
        val reviewId = anyString()
        val shopId = anyString()
        val likeStatus = anyInt()
        val expectedResponse = ToggleLikeReviewResponse()

        onToggleLikeReviewSuccess_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, likeStatus)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewSuccessEquals(Success(expectedResponse.toggleProductReviewLike))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected error`() {
        val reviewId = anyString()
        val shopId = anyString()
        val likeStatus = anyInt()
        val expectedResponse = Throwable()

        onToggleLikeReviewFail_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, likeStatus)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewErrorEquals(Fail(expectedResponse))
    }

//    @Test
//    fun `when setFilter from rating should set expected filter, reset page and make network call with correct params`() {
//
//        val selectedFilters = listOf(ListItemUnify("5", ""), ListItemUnify("3", ""))
//        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
//        val expectedRatingFilter = listOf("5","3")
//        val expectedResponse =  ProductReviewList()
//
//        onGetProductReviewsSuccess_thenReturn(expectedResponse)
//
//        viewModel.setFilter(selectedFilters, type)
//        val actualRatingFilter = viewModel.getSelectedRatingFilter()
//
//        verifyGetProductReviewListUseCase()
//        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
//        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
//    }

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

    private fun onToggleLikeReviewSuccess_thenReturn(expectedResponse: ToggleLikeReviewResponse) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onToggleLikeReviewFail_thenReturn(throwable: Throwable) {
        coEvery { toggleLikeReviewUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetProductRatingAndTopicsUseCaseExecuted() {
        coVerify { getProductRatingAndTopicsUseCase.executeOnBackground() }
    }

    private fun verifyGetProductReviewListUseCaseExecuted() {
        coVerify { getProductReviewListUseCase.executeOnBackground() }
    }

    private fun verifyToggleLikeDislikeUseCaseExecuted() {
        coVerify { toggleLikeReviewUseCase.executeOnBackground() }
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

    private fun verifyToggleLikeReviewSuccessEquals(expectedSuccessValue: Success<ToggleProductReviewLike>) {
        viewModel.toggleLikeReview.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyToggleLikeReviewErrorEquals(expectedErrorValue: Fail) {
        viewModel.toggleLikeReview.verifyErrorEquals(expectedErrorValue)
    }
}