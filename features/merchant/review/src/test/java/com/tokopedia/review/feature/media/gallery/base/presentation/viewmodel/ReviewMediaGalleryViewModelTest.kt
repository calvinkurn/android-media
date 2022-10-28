package com.tokopedia.review.feature.media.gallery.base.presentation.viewmodel

import com.google.gson.Gson
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.AdapterUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.ViewPagerUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.extension.put
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class ReviewMediaGalleryViewModelTest : ReviewMediaGalleryViewModelTestFixture() {
    @Test
    fun `onPageSelected should update _viewPagerUiState when changingConfiguration is false`() = runBlockingTest {
        val newPosition = 10
        val currentPosition = viewModel.viewPagerUiState.first().currentPagerPosition
        viewModel.updateOrientationUiState(OrientationUiState(OrientationUiState.Orientation.PORTRAIT))
        viewModel.onPageSelected(newPosition)
        Assert.assertEquals(currentPosition, viewModel.viewPagerUiState.first().previousPagerPosition)
        Assert.assertEquals(newPosition, viewModel.viewPagerUiState.first().currentPagerPosition)
    }

    @Test
    fun `onPageSelected should not update _viewPagerUiState when changingConfiguration is true`() = runBlockingTest {
        val newPosition = 10
        val currentPosition = viewModel.viewPagerUiState.first().currentPagerPosition
        val previousPosition = viewModel.viewPagerUiState.first().previousPagerPosition
        viewModel.updateOrientationUiState(OrientationUiState(OrientationUiState.Orientation.LANDSCAPE))
        viewModel.onPageSelected(newPosition)
        Assert.assertEquals(previousPosition, viewModel.viewPagerUiState.first().previousPagerPosition)
        Assert.assertEquals(currentPosition, viewModel.viewPagerUiState.first().currentPagerPosition)
    }

    @Test
    fun `onPageSelected should not update if new page position is equal to current page position`() = runBlockingTest {
        val initialState = viewModel.viewPagerUiState.first()
        viewModel.onPageSelected(initialState.currentPagerPosition)
        val previousState = viewModel.viewPagerUiState.first()
        viewModel.onPageSelected(previousState.currentPagerPosition)
        val currentState = viewModel.viewPagerUiState.first()
        Assert.assertTrue(previousState === currentState)
    }

    @Test
    fun `onPageSelected should disable view pager when changingConfiguration is true`() = runBlockingTest {
        val newPosition = 10
        viewModel.onPageSelected(newPosition)
        Assert.assertEquals(false, viewModel.viewPagerUiState.first().enableUserInput)
    }

    @Test
    fun `saveUiState should save current states`() = runBlockingTest {
        mockkStatic("com.tokopedia.reviewcommon.extension.CacheManagerExtKt") {
            val cacheManager = mockk<CacheManager>(relaxed = true)
            viewModel.saveUiState(cacheManager)
            verify { cacheManager.put(customId = ReviewMediaGalleryViewModel.SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE, objectToPut = any<AdapterUiState>(), gson = any<Gson>()) }
            verify { cacheManager.put(ReviewMediaGalleryViewModel.SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE, any<ViewPagerUiState>(), CacheManager.defaultExpiredDuration) }
        }
    }

    @Test
    fun `restoreUiState should restore all saved states`() = runBlockingTest {
        val latestViewPagerUiState = mockk<ViewPagerUiState>(relaxed = true)
        val cacheManager = mockk<CacheManager>(relaxed = true) {
            every {
                get(ReviewMediaGalleryViewModel.SAVED_STATE_MEDIA_GALLERY_VIEW_PAGER_UI_STATE, ViewPagerUiState::class.java, viewModel.viewPagerUiState.value)
            } returns latestViewPagerUiState
        }
        viewModel.restoreUiState(cacheManager)

        Assert.assertEquals(latestViewPagerUiState, viewModel.viewPagerUiState.first())
    }

    @Test
    fun `updateDetailedReviewMediaResult should update uiState containing load next item then update currentMediaItem`() = runBlockingTest {
        viewModel.updateDetailedReviewMediaResult(
            response = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia,
            mediaNumberToLoad = 1,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.mediaCount.toInt()
        )

        // 10 items + 1 load more item
        Assert.assertEquals(11, viewModel.uiState.first().adapterUiState.mediaItemUiModels.size)
        Assert.assertEquals(viewModel.uiState.first().adapterUiState.mediaItemUiModels.first(), viewModel.currentMediaItem.first())
    }

    @Test
    fun `updateDetailedReviewMediaResult should update uiState containing load prev item and load next then update currentMediaItem`() = runBlockingTest {
        viewModel.updateDetailedReviewMediaResult(
            response = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia,
            mediaNumberToLoad = 11,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCount.toInt()
        )

        // 10 items + 1 load more item + 1 load prev item
        val currentUiState = viewModel.uiState.first()
        Assert.assertEquals(12, currentUiState.adapterUiState.mediaItemUiModels.size)
        Assert.assertEquals(currentUiState.adapterUiState.mediaItemUiModels[currentUiState.viewPagerUiState.currentPagerPosition], viewModel.currentMediaItem.first())
    }

    @Test
    fun `updateDetailedReviewMediaResult should update uiState containing load first page then update currentMediaItem when response is null`() = runBlockingTest {
        viewModel.updateDetailedReviewMediaResult(
            response = null,
            mediaNumberToLoad = 11,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResult2ndPage.productrevGetReviewMedia.detail.mediaCount.toInt()
        )

        // 1 load first page item (1 loading state item only)
        val currentUiState = viewModel.uiState.first()
        Assert.assertEquals(1, currentUiState.adapterUiState.mediaItemUiModels.size)
        Assert.assertEquals(currentUiState.adapterUiState.mediaItemUiModels[currentUiState.viewPagerUiState.currentPagerPosition], viewModel.currentMediaItem.first())
    }

    @Test
    fun `updateDetailedReviewMediaResult should filter invalid media`() = runBlockingTest {
        viewModel.updateDetailedReviewMediaResult(
            response = getDetailedReviewMediaResultWithInvalidImageAndVideo.productrevGetReviewMedia,
            mediaNumberToLoad = 1,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithInvalidImageAndVideo.productrevGetReviewMedia.detail.mediaCount.toInt()
        )

        // 10 items - 3 invalid items (3 last item) + 1 load more item
        Assert.assertEquals(8, viewModel.uiState.first().adapterUiState.mediaItemUiModels.size)
        Assert.assertEquals(viewModel.uiState.first().adapterUiState.mediaItemUiModels.first(), viewModel.currentMediaItem.first())
    }
}