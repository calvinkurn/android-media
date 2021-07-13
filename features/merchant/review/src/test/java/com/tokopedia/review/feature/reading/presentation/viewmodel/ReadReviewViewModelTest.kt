package com.tokopedia.review.feature.reading.presentation.viewmodel

import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.review.common.data.ToggleProductReviewLike
import com.tokopedia.review.feature.reading.data.*
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.review.feature.reading.presentation.uimodel.ToggleLikeUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
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
    fun `when productId is not set should return empty`() {
        val expectedProductId = ""

        val actualProductId = viewModel.getProductId()

        Assert.assertEquals(expectedProductId, actualProductId)
    }

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected results`() {
        val productId = anyString()
        val expectedResponse = ProductRatingAndTopic(ProductrevGetProductRatingAndTopic(
                ProductRating(satisfactionRate = "90% pembeli merasa puas",
                        detail = listOf(
                                ProductReviewDetail(5, "70", 70F),
                                ProductReviewDetail(4, "10", 10F),
                                ProductReviewDetail(3, "10", 10F),
                                ProductReviewDetail(2, "10", 10F),
                                ProductReviewDetail(1, "0", 0F),
                        ))))

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        val actualProductId = viewModel.getProductId()

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedResponse.productrevGetProductRatingAndTopics))
        Assert.assertEquals(productId, actualProductId)
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
        val likeStatus = 1
        val totalLike = 1
        val index = anyInt()
        val expectedResponse = ToggleLikeReviewResponse(ToggleProductReviewLike(likeStatus = likeStatus, totalLike = 1))
        val expectedValue = ToggleLikeUiModel(likeStatus, totalLike, index)

        onToggleLikeReviewSuccess_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewSuccessEquals(Success(expectedValue))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected error`() {
        val reviewId = anyString()
        val shopId = anyString()
        val likeStatus = anyInt()
        val index = anyInt()
        val expectedResponse = Throwable()

        onToggleLikeReviewFail_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, shopId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when clearFilters should make all filters null`() {
        val expectedSelectedRating = emptySet<String>()
        val expectedSelectedTopic = emptySet<String>()

        val actualSelectedRating = viewModel.getSelectedRatingFilter()
        val actualSelectedTopic = viewModel.getSelectedTopicFilter()
        viewModel.clearFilters()

        Assert.assertFalse(viewModel.isFilterSelected())
        Assert.assertEquals(expectedSelectedRating, actualSelectedRating)
        Assert.assertEquals(expectedSelectedTopic, actualSelectedTopic)
    }

    @Test
    fun `when setFilterWithImage but isActive = true should make it null`() {
        val isActive = true
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive)
        viewModel.setFilterWithImage(isActive)

        Assert.assertFalse(viewModel.isFilterSelected())
        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setFilter from rating should set expected filter, reset page and make network call with correct params`() {
        val ratingFive = "5"
        val ratingThree = "3"
        val emptyDescription = ""
        val selectedFilters = setOf(ListItemUnify(ratingFive, emptyDescription), ListItemUnify(ratingThree, emptyDescription))
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf(ratingFive, ratingThree)
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter from topic should set expected filter, reset page and make network call with correct params`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val productQualityTopicKey = "kualitas"
        val shopServiceTopicKey = "pelayanan"
        val expectedRatingAndTopicResponse = ProductRatingAndTopic(ProductrevGetProductRatingAndTopic(topics = listOf(ProductTopic(formatted = productQualityTopic, key = productQualityTopicKey), ProductTopic(formatted = shopServiceTopic, key = shopServiceTopicKey))))
        val emptyDescription = ""
        val selectedFilters = setOf(ListItemUnify(productQualityTopic, emptyDescription), ListItemUnify(shopServiceTopic, emptyDescription))
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf(productQualityTopic, shopServiceTopic)
        val expectedResponse = ProductReviewList()

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedRatingAndTopicResponse.productrevGetProductRatingAndTopics))

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type)
        val actualTopicFilter = viewModel.getSelectedTopicFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when setFilter from rating bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf<String>()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter from topic bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf<String>()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type)
        val actualTopicFilter = viewModel.getSelectedTopicFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when setSort to invalid value should set to default value`() {
        val sort = ""
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setSort(sort)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
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

    private fun verifyToggleLikeReviewSuccessEquals(expectedSuccessValue: Success<ToggleLikeUiModel>) {
        viewModel.toggleLikeReview.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyToggleLikeReviewErrorEquals(expectedErrorValue: Fail) {
        viewModel.toggleLikeReview.verifyErrorEquals(expectedErrorValue)
    }
}