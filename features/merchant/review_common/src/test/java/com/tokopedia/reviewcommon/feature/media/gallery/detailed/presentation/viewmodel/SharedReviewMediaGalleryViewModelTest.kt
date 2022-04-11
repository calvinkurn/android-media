package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevLikeReview
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ToggleLikeReviewResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.ToasterUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class SharedReviewMediaGalleryViewModelTest: SharedReviewMediaGalleryViewModelTestFixture() {
    @Test
    fun `retryGetReviewMedia should call getReviewMedia and return success`() = runBlockingTest {
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(any(), any())
            } returns "123456"

            every {
                get<Any>(any(), any(), any())
            } returns null
        }
        viewModel.tryGetPreloadedData(cacheManager)
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))

        coEvery {
            getDetailedReviewMediaUseCase.executeOnBackground()
        } returns getDetailedReviewMediaResult1stPage

        viewModel.retryGetReviewMedia()

        Assert.assertEquals(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia, viewModel.detailedReviewMediaResult.first())
    }

    @Test
    fun `saveState should save current states`() {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveState(outState)
        verify { outState.putSerializable(SharedReviewMediaGalleryViewModel.SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, any()) }
        verify { outState.putString(SharedReviewMediaGalleryViewModel.SAVED_STATE_PRODUCT_ID, any()) }
        verify { outState.putBoolean(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_SEE_MORE, any()) }
        verify { outState.putSerializable(SharedReviewMediaGalleryViewModel.SAVED_STATE_ORIENTATION_UI_STATE, any()) }
        verify { outState.putBoolean(SharedReviewMediaGalleryViewModel.SAVED_STATE_OVERLAY_VISIBILITY, any()) }
        verify { outState.putBoolean(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, any()) }
    }

    @Test
    fun `restoreState should restore latest states`() = runBlockingTest {
        val latestGetDetailedReviewMediaResult = mockk<ProductrevGetReviewMedia>(relaxed = true)
        val latestProductID = "123456"
        val latestShowSeeMore = true
        val latestOrientationUiState = OrientationUiState.Landscape
        val latestOverlayVisibility = false
        val latestShowActionMenuBottomSheet = true
        val savedState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_GET_DETAILED_REVIEW_MEDIA_RESULT, viewModel.detailedReviewMediaResult.value)
            } returns latestGetDetailedReviewMediaResult
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_PRODUCT_ID, viewModel.getProductId())
            } returns latestProductID
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_SEE_MORE, viewModel.showSeeMore.value)
            } returns latestShowSeeMore
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_ORIENTATION_UI_STATE, viewModel.orientationUiState.value)
            } returns latestOrientationUiState
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_OVERLAY_VISIBILITY, viewModel.overlayVisibility.value)
            } returns latestOverlayVisibility
            every {
                getSavedState(SharedReviewMediaGalleryViewModel.SAVED_STATE_SHOW_ACTION_MENU_BOTTOM_SHEET, false)
            } returns latestShowActionMenuBottomSheet
        }
        viewModel.updateReviewDetailItem(mockk(relaxed = true))
        viewModel.restoreState(savedState)

        Assert.assertEquals(latestGetDetailedReviewMediaResult, viewModel.detailedReviewMediaResult.first())
        Assert.assertEquals(latestProductID, viewModel.getProductId())
        Assert.assertEquals(latestShowSeeMore, viewModel.showSeeMore.first())
        Assert.assertEquals(latestOrientationUiState, viewModel.orientationUiState.first())
        Assert.assertEquals(latestOverlayVisibility, viewModel.overlayVisibility.first())
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Showing)
    }

    @Test
    fun `tryGetPreloadedData should get all given data from cache manager`() = runBlockingTest {
        val preloadedTargetMediaNumber = 1
        val preloadedShowSeeMore = true
        val preloadedDetailedReviewMediaResult = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia
        val preloadedProductID = "123456"
        val preloadedShopID = "654321"
        val preloadedIsProductReview = true
        val preloadedIsFromGallery = true
        val mockCacheManager = mockk<CacheManager> {
            every { get<Any>(any(), any(), any()) } answers {
                when (firstArg<String>()) {
                    SharedReviewMediaGalleryViewModel.EXTRAS_TARGET_MEDIA_NUMBER -> preloadedTargetMediaNumber
                    SharedReviewMediaGalleryViewModel.EXTRAS_SHOW_SEE_MORE -> preloadedShowSeeMore
                    SharedReviewMediaGalleryViewModel.EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT -> preloadedDetailedReviewMediaResult
                    SharedReviewMediaGalleryViewModel.EXTRAS_IS_PRODUCT_REVIEW -> preloadedIsProductReview
                    SharedReviewMediaGalleryViewModel.EXTRAS_IS_FROM_GALLERY -> preloadedIsFromGallery
                    else -> null
                }
            }
            every { getString(any(), any()) } answers {
                when (firstArg<String>()) {
                    SharedReviewMediaGalleryViewModel.EXTRAS_PRODUCT_ID -> preloadedProductID
                    SharedReviewMediaGalleryViewModel.EXTRAS_SHOP_ID -> preloadedShopID
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
    }

    @Test
    fun `updateCurrentMediaItem should update _currentMediaItem`() = runBlockingTest {
        val currentMediaItem = LoadingStateItemUiModel(mediaNumber = 1)
        viewModel.updateCurrentMediaItem(currentMediaItem)
        Assert.assertEquals(currentMediaItem, viewModel.currentMediaItem.first())
    }

    @Test
    fun `updateReviewDetailItem should update _currentReviewDetail`() = runBlockingTest {
        val currentReviewDetailItem = mockk<ReviewDetailUiModel>(relaxed = true)
        viewModel.updateReviewDetailItem(currentReviewDetailItem)
        Assert.assertEquals(currentReviewDetailItem, viewModel.currentReviewDetail.first())
    }

    @Test
    fun `requestToggleLike should send toggle like review request and update like count`() = runBlockingTest {
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
        viewModel.requestToggleLike(feedbackID, invertedLikeStatus)

        val newTotalLike = viewModel.detailedReviewMediaResult.first()!!.detail.reviewDetail.first().totalLike
        Assert.assertEquals(currentTotalLike + 1, newTotalLike)
    }

    @Test
    fun `requestPortraitMode should change _orientationUiState to OrientationUiState#Portrait`() = runBlockingTest {
        viewModel.requestPortraitMode()
        Assert.assertEquals(OrientationUiState.Portrait, viewModel.orientationUiState.first())
    }

    @Test
    fun `requestLandscapeMode should change _orientationUiState to OrientationUiState#Landscape`() = runBlockingTest {
        viewModel.requestLandscapeMode()
        Assert.assertEquals(OrientationUiState.Landscape, viewModel.orientationUiState.first())
    }

    @Test
    fun `toggleOverlayVisibility should change _overlayVisibility from true to false vice versa`() = runBlockingTest {
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
    fun `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Showing if _currentReviewDetail is not null`() = runBlockingTest {
        viewModel.updateReviewDetailItem(mockk(relaxed = true))
        viewModel.showActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Showing)
    }

    @Test
    fun `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#hidden if _currentReviewDetail is null`() = runBlockingTest {
        viewModel.showActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Hidden)
    }

    @Test
    fun `dismissActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Hidden`() = runBlockingTest {
        `showActionMenuBottomSheet should change actionMenuBottomSheetUiState to ActionMenuBottomSheetUiState#Showing if _currentReviewDetail is not null`()
        viewModel.dismissActionMenuBottomSheet()
        Assert.assertTrue(viewModel.actionMenuBottomSheetUiState.first() is ActionMenuBottomSheetUiState.Hidden)
    }

    @Test
    fun `enqueueToaster should notify collectors`() {
        val mockToasterData = mockk<ToasterUiModel>()
        var latestToasterData: ToasterUiModel? = null
        runBlockingTest {
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
        runBlockingTest {
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
    fun `getReviewMedia should merge old result with new result when load more next`() = runBlockingTest {
        val mockCacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(SharedReviewMediaGalleryViewModel.EXTRAS_PRODUCT_ID, viewModel.getProductId())
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
    fun `getReviewMedia should merge old result with new result when load more previous`() = runBlockingTest {
        val mockCacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                getString(SharedReviewMediaGalleryViewModel.EXTRAS_PRODUCT_ID, viewModel.getProductId())
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
}