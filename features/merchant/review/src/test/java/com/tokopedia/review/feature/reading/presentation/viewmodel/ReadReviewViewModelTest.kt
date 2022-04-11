package com.tokopedia.review.feature.reading.presentation.viewmodel

import com.tokopedia.review.common.data.ProductrevLikeReview
import com.tokopedia.review.common.data.ToggleLikeReviewResponse
import com.tokopedia.review.feature.reading.data.Attachments
import com.tokopedia.review.feature.reading.data.Product
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductReviewList
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopReviewList
import com.tokopedia.review.feature.reading.data.ShopRatingAndTopic
import com.tokopedia.review.feature.reading.data.ShopReview
import com.tokopedia.review.feature.reading.data.ShopReviewList
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
import io.mockk.every
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
    fun `when setId is not set should return empty`() {
        val expectedShopId = ""

        val actualShopId = viewModel.getShopId()

        Assert.assertEquals(expectedShopId, actualShopId)
    }

    @Test
    fun `isLoggedIn should return mock value`() {
        val mockIsLoggedInValue = true
        every {
            userSessionInterface.isLoggedIn
        } returns mockIsLoggedInValue
        val actualIsLoggedIn = viewModel.isLoggedIn

        Assert.assertEquals(mockIsLoggedInValue, actualIsLoggedIn)
    }

    @Test
    fun `when setProductId should call getRatingAndTopics and return expected results`() {
        val productId = anyString()
        val expectedResponse = ProductRatingAndTopic(
            ProductrevGetProductRatingAndTopic(
                ProductRating(
                    satisfactionRate = "90% pembeli merasa puas",
                    detail = listOf(
                        ProductReviewDetail(5, "70", 70F),
                        ProductReviewDetail(4, "10", 10F),
                        ProductReviewDetail(3, "10", 10F),
                        ProductReviewDetail(2, "10", 10F),
                        ProductReviewDetail(1, "0", 0F),
                    )
                )
            )
        )

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse)

        viewModel.setProductId(productId)
        val actualProductId = viewModel.getProductId()

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedResponse.productrevGetProductRatingAndTopics))
        Assert.assertEquals(productId, actualProductId)
    }

    @Test
    fun `when setShopId should call getShopRatingAndTopics and return expected results`() {
        val shopId = anyString()
        val expectedResponse = ShopRatingAndTopic(
                ProductrevGetShopRatingAndTopic(
                        ProductRating(
                                satisfactionRate = "90% pembeli merasa puas",
                                detail = listOf(
                                        ProductReviewDetail(5, "70", 70F),
                                        ProductReviewDetail(4, "10", 10F),
                                        ProductReviewDetail(3, "10", 10F),
                                        ProductReviewDetail(2, "10", 10F),
                                        ProductReviewDetail(1, "0", 0F),
                                )
                        )
                )
        )

        onGetShopRatingAndTopicsSuccess_thenReturn(expectedResponse)

        viewModel.setShopId(shopId)
        val actualShopId = viewModel.getShopId()

        verifyGetShopRatingAndTopicsUseCaseExecuted()
        verifyShopRatingAndTopicSuccessEquals(Success(expectedResponse.productrevGetShopRatingAndTopics))
        Assert.assertEquals(shopId, actualShopId)
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

        viewModel.setPage(page, true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setPage should call getShopReviews and return expected results`() {
        val page = anyInt()
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setPage(page, false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
    }

    @Test
    fun `when setPage should call getProductReviews and return expected error`() {
        val page = anyInt()
        val expectedResponse = Throwable()

        onGetProductReviewsFail_thenReturn(expectedResponse)

        viewModel.setPage(page, true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when setPage should call getShopReviews and return expected error`() {
        val page = anyInt()
        val expectedResponse = Throwable()

        onGetShopReviewsFail_thenReturn(expectedResponse)

        viewModel.setPage(page, false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when mapProductReviewToReadReviewUiModel should return expected list of ReadReviewUiModel`() {
        val productReviews = listOf(
            ProductReview(feedbackID = "1"),
            ProductReview(feedbackID = "2"),
            ProductReview(feedbackID = "3"),
            ProductReview(feedbackID = "4"),
            ProductReview(feedbackID = "5")
        )
        val isShopViewHolder = false
        val shopId = "mockShopId"
        val shopName = "My Shop"
        val productId = "129123"
        val expectedReadReviewUiModelList = listOf(
            ReadReviewUiModel(reviewData = ProductReview("1"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("2"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("3"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("4"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("5"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
        )

        viewModel.setProductId(productId)
        val result = viewModel.mapProductReviewToReadReviewUiModel(productReviews, shopId, shopName)

        result.forEachIndexed { index, readReviewUiModel ->
            with(expectedReadReviewUiModelList[index]) {
                Assert.assertEquals(readReviewUiModel.reviewData.feedbackID, reviewData.feedbackID)
                Assert.assertEquals(readReviewUiModel.isShopViewHolder, isShopViewHolder)
                Assert.assertEquals(readReviewUiModel.shopId, shopId)
                Assert.assertEquals(readReviewUiModel.shopName, shopName)
                Assert.assertEquals(readReviewUiModel.productId, productId)
            }
        }
    }

    @Test
    fun `when mapShopReviewToReadReviewUiModel should return expected list of ReadReviewUiModel`() {
        val productId = "129123"
        val shopReviews = listOf(
                ShopReview(reviewID = "1", product = Product(productID = productId), attachments = listOf(Attachments())),
                ShopReview(reviewID = "2", product = Product(productID = productId))
        )
        val isShopViewHolder = true
        val shopId = "423543"
        val shopName = "My Shop"
        val expectedReadReviewUiModelList = listOf(
                ReadReviewUiModel(reviewData = ProductReview("1"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
                ReadReviewUiModel(reviewData = ProductReview("2"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId)
        )
        viewModel.setShopId(productId)
        val result = viewModel.mapShopReviewToReadReviewUiModel(shopReviews, shopId, shopName)

        result.forEachIndexed { index, readReviewUiModel ->
            with(expectedReadReviewUiModelList[index]) {
                Assert.assertEquals(readReviewUiModel.reviewData.feedbackID, reviewData.feedbackID)
                Assert.assertEquals(readReviewUiModel.isShopViewHolder, isShopViewHolder)
                Assert.assertEquals(readReviewUiModel.shopId, shopId)
                Assert.assertEquals(readReviewUiModel.shopName, shopName)
                Assert.assertEquals(readReviewUiModel.productId, productId)
            }
        }
    }

    @Test
    fun `when productId is null should set empty productId when mapping`() {
        val productReviews = listOf(
            ProductReview(feedbackID = "1"),
            ProductReview(feedbackID = "2"),
            ProductReview(feedbackID = "3"),
            ProductReview(feedbackID = "4"),
            ProductReview(feedbackID = "5")
        )
        val isShopViewHolder = false
        val shopId = "mockShopId"
        val shopName = "My Shop"
        val productId = ""
        val expectedReadReviewUiModelList = listOf(
            ReadReviewUiModel(reviewData = ProductReview("1"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("2"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("3"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("4"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
            ReadReviewUiModel(reviewData = ProductReview("5"), isShopViewHolder = isShopViewHolder, shopId = shopId, shopName = shopName, productId = productId),
        )

        val result = viewModel.mapProductReviewToReadReviewUiModel(productReviews, shopId, shopName)

        result.forEachIndexed { index, readReviewUiModel ->
            with(expectedReadReviewUiModelList[index]) {
                Assert.assertEquals(readReviewUiModel.reviewData.feedbackID, reviewData.feedbackID)
                Assert.assertEquals(readReviewUiModel.isShopViewHolder, isShopViewHolder)
                Assert.assertEquals(readReviewUiModel.shopId, shopId)
                Assert.assertEquals(readReviewUiModel.shopName, shopName)
                Assert.assertEquals(readReviewUiModel.productId, productId)
            }
        }
    }

    @Test
    fun `when setSort should set expected sort and make network call`() {
        val sort = SortTypeConstants.MOST_HELPFUL_PARAM
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setSort(sort, true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setSort on shop review should set expected sort and make network call`() {
        val sort = SortTypeConstants.LATEST_PARAM
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setSort(sort, false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
    }

    @Test
    fun `when setFilterWithImage should set expected filter and make network call`() {
        val isActive = false
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive, true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setFilterWithImage on shop review should set expected filter and make network call`() {
        val isActive = false
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive, false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected results`() {
        val reviewId = anyString()
        val likeStatus = 1
        val totalLike = 1
        val index = anyInt()
        val expectedResponse = ToggleLikeReviewResponse(
            ProductrevLikeReview(
                likeStatus = likeStatus,
                totalLike = 1
            )
        )
        val expectedValue = ToggleLikeUiModel(likeStatus, totalLike, index)

        onToggleLikeReviewSuccess_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewSuccessEquals(Success(expectedValue))
    }

    @Test
    fun `when toggleLike shop review should call toggleLikeReviewUseCase and return expected results`() {
        val reviewId = anyString()
        val likeStatus = 1
        val totalLike = 1
        val index = anyInt()
        val expectedResponse = ToggleLikeReviewResponse(
                ProductrevLikeReview(
                        likeStatus = likeStatus,
                        totalLike = 1
                )
        )
        val expectedValue = ToggleLikeUiModel(likeStatus, totalLike, index)

        onToggleLikeReviewSuccess_thenReturn(expectedResponse)

        viewModel.toggleLikeShopReview(reviewId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewSuccessEquals(Success(expectedValue))
    }

    @Test
    fun `when toggleLike should call toggleLikeReviewUseCase and return expected error`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val index = anyInt()
        val expectedResponse = Throwable()

        onToggleLikeReviewFail_thenReturn(expectedResponse)

        viewModel.toggleLikeReview(reviewId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when toggleLike shop review should call toggleLikeReviewUseCase and return expected error`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val index = anyInt()
        val expectedResponse = Throwable()

        onToggleLikeReviewFail_thenReturn(expectedResponse)

        viewModel.toggleLikeShopReview(reviewId, likeStatus, index)

        verifyToggleLikeDislikeUseCaseExecuted()
        verifyToggleLikeReviewErrorEquals(Fail(expectedResponse))
    }

    @Test
    fun `when clearFilters should make all filters null`() {
        val expectedSelectedRating = emptySet<String>()
        val expectedSelectedTopic = emptySet<String>()

        viewModel.clearFilters()
        val actualSelectedRating = viewModel.getSelectedRatingFilter()
        val actualSelectedTopic = viewModel.getSelectedTopicFilter(true)

        Assert.assertFalse(viewModel.isFilterSelected())
        Assert.assertEquals(expectedSelectedRating, actualSelectedRating)
        Assert.assertEquals(expectedSelectedTopic, actualSelectedTopic)
    }

    @Test
    fun `when clearFilters shop review should make all filters null`() {
        val expectedSelectedRating = emptySet<String>()
        val expectedSelectedTopic = emptySet<String>()

        viewModel.clearFilters()
        val actualSelectedRating = viewModel.getSelectedRatingFilter()
        val actualSelectedTopic = viewModel.getSelectedTopicFilter(false)

        Assert.assertFalse(viewModel.isFilterSelected())
        Assert.assertEquals(expectedSelectedRating, actualSelectedRating)
        Assert.assertEquals(expectedSelectedTopic, actualSelectedTopic)
    }

    @Test
    fun `when setFilter from topic but invalid topic should get expected filter`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val expectedRatingAndTopicResponse = Throwable()
        val emptyDescription = ""
        val selectedFilters = setOf(
            ListItemUnify(productQualityTopic, emptyDescription),
            ListItemUnify(shopServiceTopic, emptyDescription)
        )
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet

        onGetProductRatingAndTopicsFail_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicErrorEquals(Fail(expectedRatingAndTopicResponse))

        viewModel.setFilter(selectedFilters, type, true)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(true)

        Assert.assertTrue(actualTopicFilter.isEmpty())
    }

    @Test
    fun `when setFilter shop review from topic but invalid topic should get expected filter`() {
        val shopId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val expectedRatingAndTopicResponse = Throwable()
        val emptyDescription = ""
        val selectedFilters = setOf(
                ListItemUnify(productQualityTopic, emptyDescription),
                ListItemUnify(shopServiceTopic, emptyDescription)
        )
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet

        onGetShopRatingAndTopicsFail_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setShopId(shopId)

        verifyGetShopRatingAndTopicsUseCaseExecuted()
        verifyShopRatingAndTopicErrorEquals(Fail(expectedRatingAndTopicResponse))

        viewModel.setFilter(selectedFilters, type, false)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(false)

        Assert.assertTrue(actualTopicFilter.isEmpty())
    }

    @Test
    fun `when setFilterWithImage but isActive = true should make it null`() {
        val isActive = true
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive, true)
        viewModel.setFilterWithImage(isActive, true)

        Assert.assertFalse(viewModel.isFilterSelected())
        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
    }

    @Test
    fun `when setFilterWithImage shop review but isActive = true should make it null`() {
        val isActive = true
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterWithImage(isActive, false)
        viewModel.setFilterWithImage(isActive, false)

        Assert.assertFalse(viewModel.isFilterSelected())
        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
    }

    @Test
    fun `when setFilter from rating should set expected filter, reset page and make network call with correct params`() {
        val ratingFive = "5"
        val ratingThree = "3"
        val emptyDescription = ""
        val selectedFilters = setOf(
            ListItemUnify(ratingFive, emptyDescription),
            ListItemUnify(ratingThree, emptyDescription)
        )
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf(ratingFive, ratingThree)
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, true)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter shop review from rating should set expected filter, reset page and make network call with correct params`() {
        val ratingFive = "5"
        val ratingThree = "3"
        val emptyDescription = ""
        val selectedFilters = setOf(
                ListItemUnify(ratingFive, emptyDescription),
                ListItemUnify(ratingThree, emptyDescription)
        )
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf(ratingFive, ratingThree)
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, false)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter from topic should set expected filter, reset page and make network call with correct params`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val productQualityTopicKey = "kualitas"
        val shopServiceTopicKey = "pelayanan"
        val expectedRatingAndTopicResponse = ProductRatingAndTopic(
            ProductrevGetProductRatingAndTopic(
                topics = listOf(
                    ProductTopic(
                        formatted = productQualityTopic,
                        key = productQualityTopicKey
                    ), ProductTopic(formatted = shopServiceTopic, key = shopServiceTopicKey)
                )
            )
        )
        val emptyDescription = ""
        val selectedFilters = setOf(
            ListItemUnify(productQualityTopic, emptyDescription),
            ListItemUnify(shopServiceTopic, emptyDescription)
        )
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf(productQualityTopic, shopServiceTopic)
        val expectedResponse = ProductReviewList()

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedRatingAndTopicResponse.productrevGetProductRatingAndTopics))

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, true)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when setFilter shop review from topic should set expected filter, reset page and make network call with correct params`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val productQualityTopicKey = "kualitas"
        val shopServiceTopicKey = "pelayanan"
        val expectedRatingAndTopicResponse = ShopRatingAndTopic(
                ProductrevGetShopRatingAndTopic(
                        topics = listOf(
                                ProductTopic(
                                        formatted = productQualityTopic,
                                        key = productQualityTopicKey
                                ), ProductTopic(formatted = shopServiceTopic, key = shopServiceTopicKey)
                        )
                )
        )
        val emptyDescription = ""
        val selectedFilters = setOf(
                ListItemUnify(productQualityTopic, emptyDescription),
                ListItemUnify(shopServiceTopic, emptyDescription)
        )
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf(productQualityTopic, shopServiceTopic)
        val expectedResponse = ShopReviewList()

        onGetShopRatingAndTopicsSuccess_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setShopId(productId)

        verifyGetShopRatingAndTopicsUseCaseExecuted()
        verifyShopRatingAndTopicSuccessEquals(Success(expectedRatingAndTopicResponse.productrevGetShopRatingAndTopics))

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, false)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when selectedTopics from setFilter shop review from topic didn't match, should return empty and reset page and make network call with correct params`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val productQualityTopicKey = "kualitas"
        val shopServiceTopicKey = "pelayanan"
        val expectedRatingAndTopicResponse = ShopRatingAndTopic(
                ProductrevGetShopRatingAndTopic(
                        topics = listOf(
                                ProductTopic(
                                        formatted = productQualityTopic,
                                        key = productQualityTopicKey
                                ), ProductTopic(formatted = shopServiceTopic, key = shopServiceTopicKey)
                        )
                )
        )
        val emptyDescription = ""
        val selectedFilters = setOf(
                ListItemUnify("", emptyDescription)
        )
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf("")
        val expectedResponse = ShopReviewList()

        onGetShopRatingAndTopicsSuccess_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setShopId(productId)

        verifyGetShopRatingAndTopicsUseCaseExecuted()
        verifyShopRatingAndTopicSuccessEquals(Success(expectedRatingAndTopicResponse.productrevGetShopRatingAndTopics))

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, false)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when setFilter from rating bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf<String>()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, true)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter shop review from rating bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.RatingFilterBottomSheet
        val expectedRatingFilter = setOf<String>()
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, false)
        val actualRatingFilter = viewModel.getSelectedRatingFilter()

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
        Assert.assertEquals(expectedRatingFilter, actualRatingFilter)
    }

    @Test
    fun `when setFilter from topic bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf<String>()
        val expectedResponse = ProductReviewList()

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, true)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(true)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when setFilter shop review from topic bottom sheet is empty should set to null`() {
        val selectedFilters = setOf<ListItemUnify>()
        val type = SortFilterBottomSheetType.TopicFilterBottomSheet
        val expectedTopicFilter = setOf<String>()
        val expectedResponse = ShopReviewList()

        onGetShopReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilter(selectedFilters, type, false)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(false)

        verifyGetShopReviewListUseCaseExecuted()
        verifyShopReviewsSuccessEquals(Success(expectedResponse.productrevGetShopReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    @Test
    fun `when getUserId should get expected userId`() {
        val expectedUserId = ""
        Assert.assertEquals(expectedUserId, viewModel.userId)
    }

    @Test
    fun `when setFilterFromHighlightedTopic should set specific topic and get data`() {
        val productId = anyString()
        val productQualityTopic = "Kualitas Produk"
        val shopServiceTopic = "Pelayanan Toko"
        val productQualityTopicKey = "kualitas"
        val shopServiceTopicKey = "pelayanan"
        val isProductReview = true
        val expectedRatingAndTopicResponse = ProductRatingAndTopic(
            ProductrevGetProductRatingAndTopic(
                topics = listOf(
                    ProductTopic(
                        formatted = productQualityTopic,
                        key = productQualityTopicKey
                    ), ProductTopic(formatted = shopServiceTopic, key = shopServiceTopicKey)
                )
            )
        )
        val expectedTopicFilter = setOf(productQualityTopic)
        val expectedResponse = ProductReviewList()

        onGetProductRatingAndTopicsSuccess_thenReturn(expectedRatingAndTopicResponse)

        viewModel.setProductId(productId)

        verifyGetProductRatingAndTopicsUseCaseExecuted()
        verifyRatingAndTopicSuccessEquals(Success(expectedRatingAndTopicResponse.productrevGetProductRatingAndTopics))

        onGetProductReviewsSuccess_thenReturn(expectedResponse)

        viewModel.setFilterFromHighlightedTopic(productQualityTopic, isProductReview)
        val actualTopicFilter = viewModel.getSelectedTopicFilter(isProductReview)

        verifyGetProductReviewListUseCaseExecuted()
        verifyProductReviewsSuccessEquals(Success(expectedResponse.productrevGetProductReviewList))
        Assert.assertEquals(expectedTopicFilter, actualTopicFilter)
    }

    private fun onGetProductRatingAndTopicsSuccess_thenReturn(expectedResponse: ProductRatingAndTopic) {
        coEvery { getProductRatingAndTopicsUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetShopRatingAndTopicsSuccess_thenReturn(expectedResponse: ShopRatingAndTopic) {
        coEvery { getShopRatingAndTopicsUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductRatingAndTopicsFail_thenReturn(throwable: Throwable) {
        coEvery { getProductRatingAndTopicsUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetShopRatingAndTopicsFail_thenReturn(throwable: Throwable) {
        coEvery { getShopRatingAndTopicsUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetProductReviewsSuccess_thenReturn(expectedResponse: ProductReviewList) {
        coEvery { getProductReviewListUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetShopReviewsSuccess_thenReturn(expectedResponse: ShopReviewList) {
        coEvery { getShopReviewListUseCase.executeOnBackground() } returns expectedResponse
    }

    private fun onGetProductReviewsFail_thenReturn(throwable: Throwable) {
        coEvery { getProductReviewListUseCase.executeOnBackground() } throws throwable
    }

    private fun onGetShopReviewsFail_thenReturn(throwable: Throwable) {
        coEvery { getShopReviewListUseCase.executeOnBackground() } throws throwable
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

    private fun verifyGetShopRatingAndTopicsUseCaseExecuted() {
        coVerify { getShopRatingAndTopicsUseCase.executeOnBackground() }
    }

    private fun verifyGetProductReviewListUseCaseExecuted() {
        coVerify { getProductReviewListUseCase.executeOnBackground() }
    }

    private fun verifyGetShopReviewListUseCaseExecuted() {
        coVerify { getShopReviewListUseCase.executeOnBackground() }
    }

    private fun verifyToggleLikeDislikeUseCaseExecuted() {
        coVerify { toggleLikeReviewUseCase.executeOnBackground() }
    }

    private fun verifyRatingAndTopicSuccessEquals(expectedSuccessValue: Success<ProductrevGetProductRatingAndTopic>) {
        viewModel.ratingAndTopic.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyShopRatingAndTopicSuccessEquals(expectedSuccessValue: Success<ProductrevGetShopRatingAndTopic>) {
        viewModel.shopRatingAndTopic.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyRatingAndTopicErrorEquals(expectedErrorValue: Fail) {
        viewModel.ratingAndTopic.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyShopRatingAndTopicErrorEquals(expectedErrorValue: Fail) {
        viewModel.shopRatingAndTopic.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyProductReviewsSuccessEquals(expectedSuccessValue: Success<ProductrevGetProductReviewList>) {
        viewModel.productReviews.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyShopReviewsSuccessEquals(expectedSuccessValue: Success<ProductrevGetShopReviewList>) {
        viewModel.shopReviews.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyProductReviewsErrorEquals(expectedErrorValue: Fail) {
        viewModel.productReviews.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyShopReviewsErrorEquals(expectedErrorValue: Fail) {
        viewModel.shopReviews.verifyErrorEquals(expectedErrorValue)
    }

    private fun verifyToggleLikeReviewSuccessEquals(expectedSuccessValue: Success<ToggleLikeUiModel>) {
        viewModel.toggleLikeReview.verifySuccessEquals(expectedSuccessValue)
    }

    private fun verifyToggleLikeReviewErrorEquals(expectedErrorValue: Fail) {
        viewModel.toggleLikeReview.verifyErrorEquals(expectedErrorValue)
    }
}