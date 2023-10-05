package com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel

import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.detailed.domain.model.ProductrevLikeReview
import com.tokopedia.review.feature.media.gallery.detailed.domain.model.ToggleLikeReviewResponse
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uimodel.ToasterUiModel
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test

class SharedReviewMediaGalleryViewModelTest: SharedReviewMediaGalleryViewModelTestFixture() {
    @Test
    fun `retryGetReviewMedia should call getReviewMedia and return success`() = rule.runTest {
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(any(), any())
            } returns "123456"

            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        var count = 0

        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } answers {
            count++
            when (count) {
                1 -> throw Exception()
                else -> getDetailedReviewMediaResult1stPage
            }
        }

        viewModel.tryGetPreloadedData(cacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))

        Assert.assertNull(viewModel.detailedReviewMediaResult.first())

        viewModel.retryGetReviewMedia()

        Assert.assertEquals(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia, viewModel.detailedReviewMediaResult.first())
    }

    @Test
    fun `retryGetReviewMedia should not call getReviewMedia when already loaded`() = rule.runTest {
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(any(), any())
            } returns "123456"

            every {
                get<Any>(any(), any(), any())
            } returns null
        }

        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } returns getDetailedReviewMediaResult1stPage

        viewModel.tryGetPreloadedData(cacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))

        Assert.assertEquals(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia, viewModel.detailedReviewMediaResult.first())

        viewModel.retryGetReviewMedia()

        Assert.assertEquals(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia, viewModel.detailedReviewMediaResult.first())
    }

    @Test
    fun `retryGetReviewMedia should call getReviewMedia when not yet loaded`() = rule.runTest {
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(any(), any())
            } returns "123456"

            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        var count = 0

        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } answers {
            count++
            when (count) {
                1 -> getDetailedReviewMediaResult1stPage
                2 -> throw Exception()
                else -> getDetailedReviewMediaResult2ndPage
            }
        }

        viewModel.tryGetPreloadedData(cacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))

        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )

        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 11))

        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )

        viewModel.retryGetReviewMedia()

        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 20)
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia[10]
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
    }

    @Test
    fun `saveState should save current states`() {
        val cacheManager = mockk<CacheManager>(relaxed = true)
        viewModel.saveState(cacheManager)
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, any<ProductrevGetReviewMedia>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_PRODUCT_ID, any<String>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_SEE_MORE, any<Boolean>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_ORIENTATION_UI_STATE, any<OrientationUiState>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_OVERLAY_VISIBILITY, any<Boolean>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, any<Boolean>()) }
        verify { cacheManager.put(SharedReviewMediaGalleryViewModel.SAVED_STATE_HAS_SUCCESS_TOGGLE_LIKE_STATUS, any<Boolean>()) }
    }

    @Test
    fun `restoreState should restore latest states`() = rule.runTest {
        val latestGetDetailedReviewMediaResult = mockk<ProductrevGetReviewMedia>(relaxed = true)
        val latestProductID = "123456"
        val latestShowSeeMore = true
        val latestOrientationUiState = OrientationUiState(OrientationUiState.Orientation.LANDSCAPE)
        val latestOverlayVisibility = false
        val latestShowActionMenuBottomSheet = true
        val latestHasSuccessToggleLikeStatus = false
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, ProductrevGetReviewMedia::class.java, viewModel.detailedReviewMediaResult.value)
            } returns latestGetDetailedReviewMediaResult
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_PRODUCT_ID, String::class.java, viewModel.getProductId())
            } returns latestProductID
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_SEE_MORE, Boolean::class.java, viewModel.showSeeMore.value)
            } returns latestShowSeeMore
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_ORIENTATION_UI_STATE, OrientationUiState::class.java, viewModel.orientationUiState.value)
            } returns latestOrientationUiState
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_OVERLAY_VISIBILITY, Boolean::class.java, viewModel.overlayVisibility.value)
            } returns latestOverlayVisibility
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, Boolean::class.java, false)
            } returns latestShowActionMenuBottomSheet
            every {
                get(SharedReviewMediaGalleryViewModel.SAVED_STATE_HAS_SUCCESS_TOGGLE_LIKE_STATUS, Boolean::class.java, false)
            } returns latestHasSuccessToggleLikeStatus
        }
        viewModel.updateReviewDetailItem(mockk(relaxed = true))
        viewModel.restoreState(cacheManager)

        Assert.assertEquals(latestGetDetailedReviewMediaResult, viewModel.detailedReviewMediaResult.first())
        Assert.assertEquals(latestProductID, viewModel.getProductId())
        Assert.assertEquals(latestShowSeeMore, viewModel.showSeeMore.first())
        Assert.assertEquals(latestOrientationUiState, viewModel.orientationUiState.first())
        Assert.assertEquals(latestOverlayVisibility, viewModel.overlayVisibility.first())
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Showing)
        Assert.assertFalse(viewModel.hasSuccessToggleLikeStatus())
    }

    @Test
    fun `tryGetPreloadedData should get all given data from cache manager`() = rule.runTest {
        val preloadedTargetMediaNumber = 1
        val preloadedShowSeeMore = true
        val preloadedDetailedReviewMediaResult = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia
        val preloadedProductID = "123456"
        val preloadedShopID = "654321"
        val preloadedIsProductReview = true
        val preloadedIsFromGallery = true
        val preloadedPageSource = ReviewMediaGalleryRouter.PageSource.REVIEW
        val preloadedIsReviewOwner = true
        val mockCacheManager = mockk<CacheManager> {
            every { get<Any>(any(), any(), any()) } answers {
                when (firstArg<String>()) {
                    ReviewMediaGalleryRouter.EXTRAS_TARGET_MEDIA_NUMBER -> preloadedTargetMediaNumber
                    ReviewMediaGalleryRouter.EXTRAS_SHOW_SEE_MORE -> preloadedShowSeeMore
                    ReviewMediaGalleryRouter.EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT -> preloadedDetailedReviewMediaResult
                    ReviewMediaGalleryRouter.EXTRAS_IS_PRODUCT_REVIEW -> preloadedIsProductReview
                    ReviewMediaGalleryRouter.EXTRAS_IS_FROM_GALLERY -> preloadedIsFromGallery
                    ReviewMediaGalleryRouter.EXTRAS_PAGE_SOURCE -> preloadedPageSource
                    ReviewMediaGalleryRouter.EXTRAS_IS_REVIEW_OWNER -> preloadedIsReviewOwner
                    else -> null
                }
            }
            every { getString(any(), any()) } answers {
                when (firstArg<String>()) {
                    ReviewMediaGalleryRouter.EXTRAS_PRODUCT_ID -> preloadedProductID
                    ReviewMediaGalleryRouter.EXTRAS_SHOP_ID -> preloadedShopID
                    else -> null
                }
            }
        }
        viewModel.tryGetPreloadedData(mockCacheManager)

        Assert.assertEquals(preloadedTargetMediaNumber, viewModel.mediaNumberToLoad.first())
        Assert.assertEquals(preloadedShowSeeMore, viewModel.showSeeMore.first())
        Assert.assertEquals(preloadedDetailedReviewMediaResult, viewModel.detailedReviewMediaResult.first())
        Assert.assertEquals(preloadedProductID, viewModel.getProductId())
        Assert.assertEquals(preloadedShopID, viewModel.getShopId())
        Assert.assertEquals(preloadedIsProductReview, viewModel.isProductReview())
        Assert.assertEquals(preloadedIsFromGallery, viewModel.isFromGallery())
        Assert.assertEquals(preloadedDetailedReviewMediaResult.detail.reviewDetail[0].feedbackId, viewModel.getFeedbackId())
        Assert.assertEquals(preloadedDetailedReviewMediaResult.detail.reviewDetail[0].user.userId, viewModel.getReviewUserID())
        Assert.assertEquals(preloadedPageSource, viewModel.getPageSource())
        Assert.assertEquals(preloadedIsReviewOwner, viewModel.isReviewOwner)
    }

    @Test
    fun `updateCurrentMediaItem should update _currentMediaItem`() = rule.runTest {
        val currentMediaItem = LoadingStateItemUiModel(mediaNumber = 1)
        viewModel.updateCurrentMediaItem(currentMediaItem)
        Assert.assertEquals(currentMediaItem, viewModel.currentMediaItem.first())
    }

    @Test
    fun `updateReviewDetailItem should update _currentReviewDetail`() = rule.runTest {
        val currentReviewDetailItem = mockk<ReviewDetailUiModel>(relaxed = true)
        viewModel.updateReviewDetailItem(currentReviewDetailItem)
        Assert.assertEquals(currentReviewDetailItem, viewModel.currentReviewDetail.first())
    }

    @Test
    fun `requestToggleLike should send toggle like review request and update like count when success`() = rule.runTest {
        val currentReviewDetailData = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.reviewDetail.first()
        val currentTotalLike = currentReviewDetailData.totalLike
        val feedbackID = currentReviewDetailData.feedbackId
        val invertedLikeStatus = if (currentReviewDetailData.isLiked) ToggleLikeReviewUseCase.NEUTRAL else ToggleLikeReviewUseCase.LIKED
        val toggleLikeReviewResult = ToggleLikeReviewResponse(
            productrevLikeReview = ProductrevLikeReview(
                feedbackId = feedbackID,
                likeStatus = invertedLikeStatus,
                totalLike = currentReviewDetailData.totalLike + 1
            )
        )

        coEvery {
            toggleLikeReviewUseCase.executeOnBackground()
        } returns toggleLikeReviewResult

        `tryGetPreloadedData should get all given data from cache manager`()

        Assert.assertEquals(viewModel.getLikeStatus(), -1)

        viewModel.requestToggleLike(feedbackID, invertedLikeStatus)

        val newTotalLike = viewModel.detailedReviewMediaResult.first()!!.detail.reviewDetail.first().totalLike
        Assert.assertEquals(currentTotalLike + 1, newTotalLike)
        Assert.assertEquals(viewModel.getLikeStatus(), invertedLikeStatus)
    }

    @Test
    fun `requestToggleLike should send toggle like review request and not update like count when failed`() = rule.runTest {
        val currentReviewDetailData = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.reviewDetail.first()
        val currentTotalLike = currentReviewDetailData.totalLike
        val feedbackID = currentReviewDetailData.feedbackId
        val invertedLikeStatus = if (currentReviewDetailData.isLiked) ToggleLikeReviewUseCase.NEUTRAL else ToggleLikeReviewUseCase.LIKED

        coEvery {
            toggleLikeReviewUseCase.executeOnBackground()
        } throws Exception()

        `tryGetPreloadedData should get all given data from cache manager`()
        viewModel.requestToggleLike(feedbackID, invertedLikeStatus)

        val newTotalLike = viewModel.detailedReviewMediaResult.first()!!.detail.reviewDetail.first().totalLike
        Assert.assertEquals(currentTotalLike, newTotalLike)
    }

    @Test
    fun `requestPortraitMode should change _orientationUiState to OrientationUiState#Portrait`() = rule.runTest {
        viewModel.requestPortraitMode()
        Assert.assertEquals(OrientationUiState(OrientationUiState.Orientation.PORTRAIT), viewModel.orientationUiState.first())
    }

    @Test
    fun `requestLandscapeMode should change _orientationUiState to OrientationUiState#Landscape`() = rule.runTest {
        viewModel.requestLandscapeMode()
        Assert.assertEquals(OrientationUiState(OrientationUiState.Orientation.LANDSCAPE), viewModel.orientationUiState.first())
    }

    @Test
    fun `toggleOverlayVisibility should change _overlayVisibility from true to false vice versa`() = rule.runTest {
        viewModel.toggleOverlayVisibility()
        Assert.assertEquals(false, viewModel.overlayVisibility.first())
        viewModel.toggleOverlayVisibility()
        Assert.assertEquals(true, viewModel.overlayVisibility.first())
    }

    @Test
    fun `getProductId should return current product id`() {
        `tryGetPreloadedData should get all given data from cache manager`()
        Assert.assertEquals("123456", viewModel.getProductId())
    }

    @Test
    fun `getShopId should return current shop id`() {
        `tryGetPreloadedData should get all given data from cache manager`()
        Assert.assertEquals("654321", viewModel.getShopId())
    }

    @Test
    fun `isProductReview should return current is product review value`() {
        `tryGetPreloadedData should get all given data from cache manager`()
        Assert.assertEquals(true, viewModel.isProductReview())
    }

    @Test
    fun `isFromGallery should return current is from gallery value`() {
        `tryGetPreloadedData should get all given data from cache manager`()
        Assert.assertEquals(true, viewModel.isFromGallery())
    }

    @Test
    fun `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Showing if _currentReviewDetail is not null`() = rule.runTest {
        viewModel.updateReviewDetailItem(mockk(relaxed = true))
        viewModel.showActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Showing)
    }

    @Test
    fun `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#hidden if _currentReviewDetail is null`() = rule.runTest {
        viewModel.showActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Hidden)
    }

    @Test
    fun `dismissActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Hidden`() = rule.runTest {
        `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Showing if _currentReviewDetail is not null`()
        viewModel.dismissActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Hidden)
    }

    @Test
    fun `enqueueToaster should notify collectors`() {
        val mockToasterData = mockk<ToasterUiModel>()
        var latestToasterData: ToasterUiModel? = null
        rule.runTest {
            val collectorJob = launch {
                viewModel.toasterQueue.collectLatest {
                    latestToasterData = it
                }
            }
            viewModel.enqueueToaster(mockToasterData)
            collectorJob.cancel()
        }
        Assert.assertEquals(mockToasterData, latestToasterData)
    }

    @Test
    fun `toasterEventActionClicked should notify collectors`() {
        var latestToasterEventActionClickQueue: String? = null
        rule.runTest {
            val collectorJob = launch {
                viewModel.toasterEventActionClickQueue.collectLatest {
                    latestToasterEventActionClickQueue = it
                }
            }
            viewModel.toasterEventActionClicked("")
            collectorJob.cancel()
        }
        Assert.assertNotNull(latestToasterEventActionClickQueue)
    }

    @Test
    fun `getTotalMediaCount should return current total media count`() {
        `tryGetPreloadedData should get all given data from cache manager`()
        Assert.assertEquals(773, viewModel.getTotalMediaCount())
    }

    @Test
    fun `getTotalMediaCount should return zero if _detailedReviewMediaResult is null`() {
        Assert.assertEquals(0, viewModel.getTotalMediaCount())
    }

    @Test
    fun `updateWifiConnectivityStatus should update wifi connectivity status`() = rule.runTest {
        viewModel.updateWifiConnectivityStatus(true)
        Assert.assertTrue(viewModel.connectedToWifi.first())
    }

    @Test
    fun `hasSuccessToggleLikeStatus should return false when there's no success toggle like request`() {
        `requestToggleLike should send toggle like review request and not update like count when failed`()
        Assert.assertFalse(viewModel.hasSuccessToggleLikeStatus())
    }

    @Test
    fun `hasSuccessToggleLikeStatus should return true when there's success toggle like request`() {
        `requestToggleLike should send toggle like review request and update like count when success`()
        Assert.assertTrue(viewModel.hasSuccessToggleLikeStatus())
    }

    @Test
    fun `updateVideoDurationMillis should update videoDurationMillis value`() = rule.runTest {
        val newVideoDurationMillis = 10000L
        viewModel.updateVideoDurationMillis(newVideoDurationMillis)
        Assert.assertEquals(newVideoDurationMillis, viewModel.videoDurationMillis.first())
    }

    @Test
    fun `getReviewMedia should merge old result with new result when load more next`() = rule.runTest {
        val mockCacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(ReviewMediaGalleryRouter.EXTRAS_PRODUCT_ID, viewModel.getProductId())
            } returns "123456"
            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        var counter = 0
        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } answers {
            if (counter.isZero()) {
                counter++
                getDetailedReviewMediaResult1stPage
            } else {
                getDetailedReviewMediaResult2ndPage
            }
        }
        viewModel.tryGetPreloadedData(mockCacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 11))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 20)
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia[10]
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
    }

    @Test
    fun `getReviewMedia should merge old result with new result when load more previous`() = rule.runTest {
        val mockCacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(ReviewMediaGalleryRouter.EXTRAS_PRODUCT_ID, viewModel.getProductId())
            } returns "123456"
            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        var counter = 0
        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } answers {
            if (counter.isZero()) {
                counter++
                getDetailedReviewMediaResult2ndPage
            } else {
                getDetailedReviewMediaResult1stPage
            }
        }
        viewModel.tryGetPreloadedData(mockCacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 11))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 20)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia[9]
        )
    }

    @Test
    fun `getReviewMedia should not be called when media is already loaded`() = rule.runTest {
        val mockCacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(ReviewMediaGalleryRouter.EXTRAS_PRODUCT_ID, viewModel.getProductId())
            } returns "123456"
            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        var counter = 0
        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } answers {
            if (counter.isZero()) {
                counter++
                getDetailedReviewMediaResult1stPage
            } else {
                getDetailedReviewMediaResult2ndPage
            }
        }
        viewModel.tryGetPreloadedData(mockCacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 2))
        Assert.assertTrue(viewModel.detailedReviewMediaResult.first()!!.reviewMedia.size == 10)
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.first(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.first()
        )
        Assert.assertEquals(
            getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.reviewMedia.last(),
            viewModel.detailedReviewMediaResult.first()!!.reviewMedia.last()
        )
    }

    @Test
    fun `hideOverlay should change overlayVisibility to false`() = rule.runTest {
        viewModel.hideOverlay()

        Assert.assertEquals(viewModel.overlayVisibility.first(), false)
    }

    @Test
    fun `updateIsPlayingVideo true should change isPlayingVideo to true`() = rule.runTest {
        viewModel.updateIsPlayingVideo(true)

        Assert.assertEquals(viewModel.isPlayingVideo.first(), true)
    }

    @Test
    fun `updateIsPlayingVideo false should change isPlayingVideo to false and overlayVisibility to true`() = rule.runTest {
        viewModel.updateIsPlayingVideo(false)

        Assert.assertEquals(viewModel.isPlayingVideo.first(), false)
        Assert.assertEquals(viewModel.overlayVisibility.first(), true)
    }
}
