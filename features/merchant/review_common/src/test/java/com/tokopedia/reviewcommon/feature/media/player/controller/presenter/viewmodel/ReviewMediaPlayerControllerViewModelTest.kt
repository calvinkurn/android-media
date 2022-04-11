package com.tokopedia.reviewcommon.feature.media.player.controller.presenter.viewmodel

import android.os.Bundle
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.viewmodel.ReviewMediaPlayerControllerViewModel
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class ReviewMediaPlayerControllerViewModelTest: ReviewMediaPlayerControllerViewModelTestFixture() {
    @Test
    fun `shouldShowVideoPlayerController should be true when _currentMediaItem is VideoMediaItemUiModel`() = runBlockingTest {
        viewModel.updateCurrentMediaItem(mockk<VideoMediaItemUiModel>(relaxed = true))
        Assert.assertTrue(viewModel.uiState.first().shouldShowVideoPlayerController)
    }

    @Test
    fun `shouldShowVideoPlayerController should be false when _currentMediaItem is not VideoMediaItemUiModel`() = runBlockingTest {
        viewModel.updateCurrentMediaItem(mockk<ImageMediaItemUiModel>(relaxed = true))
        Assert.assertFalse(viewModel.uiState.first().shouldShowVideoPlayerController)
    }

    @Test
    fun `shouldShowMediaCounter should be true when totalMedia and _currentMediaItem mediaNumber is more than zero and _currentMediaItem is not LoadingStateItemUiModel`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        viewModel.updateCurrentMediaItem(
            mockk<ImageMediaItemUiModel>(relaxed = true) {
                every { mediaNumber } returns 1
            }
        )
        Assert.assertTrue(viewModel.uiState.first().shouldShowMediaCounter)
    }

    @Test
    fun `shouldShowMediaCounter should be false when totalMedia is zero`() = runBlockingTest {
        viewModel.updateCurrentMediaItem(
            mockk<ImageMediaItemUiModel>(relaxed = true) {
                every { mediaNumber } returns 1
            }
        )
        Assert.assertFalse(viewModel.uiState.first().shouldShowMediaCounter)
    }

    @Test
    fun `shouldShowMediaCounter should be false when _currentMediaItem is null`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        viewModel.updateCurrentMediaItem(null)
        Assert.assertFalse(viewModel.uiState.first().shouldShowMediaCounter)
    }

    @Test
    fun `shouldShowMediaCounter should be false when _currentMediaItem is LoadingStateItemUiModel`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        viewModel.updateCurrentMediaItem(mockk<LoadingStateItemUiModel>(relaxed = true))
        Assert.assertFalse(viewModel.uiState.first().shouldShowMediaCounter)
    }

    @Test
    fun `shouldShowMediaCounterLoader should be true when _currentMediaItem is LoadingStateItemUiModel`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        viewModel.updateCurrentMediaItem(mockk<LoadingStateItemUiModel>(relaxed = true))
        Assert.assertTrue(viewModel.uiState.first().shouldShowMediaCounterLoader)
    }

    @Test
    fun `shouldShowMediaCounterLoader should be true when _currentMediaItem is not LoadingStateItemUiModel`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        viewModel.updateCurrentMediaItem(mockk<ImageMediaItemUiModel>(relaxed = true))
        Assert.assertFalse(viewModel.uiState.first().shouldShowMediaCounterLoader)
    }

    @Test
    fun `unmute should update _muted to false`() = runBlockingTest {
        viewModel.unmute()
        Assert.assertFalse(viewModel.uiState.first().muted)
    }

    @Test
    fun `mute should update _muted to true`() = runBlockingTest {
        viewModel.mute()
        Assert.assertTrue(viewModel.uiState.first().muted)
    }

    @Test
    fun `saveState should save current states`() = runBlockingTest {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveState(outState)
        verify { outState.putBoolean(ReviewMediaPlayerControllerViewModel.SAVED_STATE_MUTED, any()) }
    }

    @Test
    fun `restoreState should save current states`() = runBlockingTest {
        val latestMutedStatus = false
        val savedState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState(ReviewMediaPlayerControllerViewModel.SAVED_STATE_MUTED, true)
            } returns latestMutedStatus
        }
        viewModel.restoreState(savedState)
        Assert.assertFalse(viewModel.uiState.first().muted)
    }

    @Test
    fun `updateOrientationUiState should update orientationUiState`() = runBlockingTest {
        viewModel.updateOrientationUiState(OrientationUiState.Landscape)
        Assert.assertEquals(OrientationUiState.Landscape, viewModel.uiState.first().orientationUiState)
    }

    @Test
    fun `updateOverlayVisibility should update overlayVisibility`() = runBlockingTest {
        viewModel.updateOverlayVisibility(false)
        Assert.assertFalse(viewModel.uiState.first().overlayVisibility)
    }

    @Test
    fun `updateCurrentMediaItem should update currentGalleryPosition`() = runBlockingTest {
        val currentPosition = 5
        viewModel.updateCurrentMediaItem(mockk<ImageMediaItemUiModel>(relaxed = true) {
            every { mediaNumber } returns currentPosition
        })
        Assert.assertEquals(currentPosition, viewModel.uiState.first().currentGalleryPosition)
    }

    @Test
    fun `updateGetDetailedReviewMediaResult should update totalMedia`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia)
        Assert.assertEquals(getDetailedReviewMediaResult1stPage.productrevGetReviewMedia.detail.mediaCount.toInt(), viewModel.uiState.first().totalMedia)
    }
}