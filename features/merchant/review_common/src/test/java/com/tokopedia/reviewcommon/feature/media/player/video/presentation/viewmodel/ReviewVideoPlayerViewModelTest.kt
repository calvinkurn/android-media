package com.tokopedia.reviewcommon.feature.media.player.video.presentation.viewmodel

import android.graphics.Bitmap
import android.os.Bundle
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoErrorUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlaybackUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlayerUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoThumbnailUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ReviewVideoPlayerViewModelTest: ReviewVideoPlayerViewModelTestFixture() {
    @Test
    fun `setVideoUri should update _videoPlayerUiState to ReviewVideoPlayerUiState#Initial`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.Initial)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToChangingConfiguration should update _videoPlayerUiState to ReviewVideoPlayerUiState#ChangingConfiguration`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToChangingConfiguration()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.ChangingConfiguration)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `resetVideoPlayerState should update _videoPlayerUiState to ReviewVideoPlayerUiState#Initial`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.resetVideoPlayerState()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.Initial)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToRestoring should update _videoPlayerUiState to ReviewVideoPlayerUiState#RestoringState`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.RestoringState)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToReadyToPlay should update _videoPlayerUiState to ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.ReadyToPlay)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setPlaybackStateToPlaying should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Playing when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPlaying(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Playing)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToPlaying should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToPlaying(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToBuffering should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Buffering when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToBuffering(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Buffering)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToBuffering should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToBuffering(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToPaused should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Paused when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPaused(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Paused)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToPaused should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToPaused(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToPreloading should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Preloading when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPreloading(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Preloading)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToPreloading should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToPreloading(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToEnded should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Ended when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToEnded(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Ended)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToEnded should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToEnded(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToError should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Error when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToError(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Error)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToError should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToError(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToInactive should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Inactive when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToInactive(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Inactive)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToInactive should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runBlockingTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToInactive(newPosition)
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `updateVideoThumbnail should update bitmap thumbnail and retain current state type`() = testCoroutineRule.runBlockingTest {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        viewModel.updateVideoThumbnail(mockBitmap)
        viewModel.hideVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Hidden)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.value.videoThumbnail)
        val newMockBitmap = mockk<Bitmap>(relaxed = true)
        viewModel.showVideoThumbnail()
        viewModel.updateVideoThumbnail(newMockBitmap)
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Showed)
        Assert.assertEquals(newMockBitmap, viewModel.videoThumbnailUiState.value.videoThumbnail)
    }

    @Test
    fun `updateVideoThumbnail should update _videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden when bitmap is null`() = testCoroutineRule.runBlockingTest {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        viewModel.updateVideoThumbnail(mockBitmap)
        viewModel.showVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.first() is ReviewVideoThumbnailUiState.Showed)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.first().videoThumbnail)

        viewModel.updateVideoThumbnail(null)
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Hidden)
        Assert.assertNull(viewModel.videoThumbnailUiState.value.videoThumbnail)
    }

    @Test
    fun `showVideoThumbnail should update _videoThumbnailUiState to ReviewVideoThumbnailUiState#Showed when current bitmap is not null`() {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        viewModel.updateVideoThumbnail(mockBitmap)
        viewModel.hideVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Hidden)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.value.videoThumbnail)

        viewModel.showVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Showed)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.value.videoThumbnail)
    }

    @Test
    fun `showVideoThumbnail should update _videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden when current bitmap is null`() {
        viewModel.showVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Hidden)
        Assert.assertNull(viewModel.videoThumbnailUiState.value.videoThumbnail)
    }

    @Test
    fun `hideVideoThumbnail should update _videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden without changing it's bitmap`() = testCoroutineRule.runBlockingTest {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        viewModel.updateVideoThumbnail(mockBitmap)
        viewModel.showVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.first() is ReviewVideoThumbnailUiState.Showed)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.first().videoThumbnail)

        viewModel.hideVideoThumbnail()
        Assert.assertTrue(viewModel.videoThumbnailUiState.value is ReviewVideoThumbnailUiState.Hidden)
        Assert.assertEquals(mockBitmap, viewModel.videoThumbnailUiState.value.videoThumbnail)
    }

    @Test
    fun `showVideoError should update _videoErrorUiState to ReviewVideoErrorUiState#Showing`() = testCoroutineRule.runBlockingTest {
        viewModel.showVideoError()
        Assert.assertEquals(ReviewVideoErrorUiState.Showing, viewModel.videoErrorUiState.first())
    }

    @Test
    fun `hideVideoError should update _videoErrorUiState to ReviewVideoErrorUiState#Hidden`() = testCoroutineRule.runBlockingTest {
        viewModel.hideVideoError()
        Assert.assertEquals(ReviewVideoErrorUiState.Hidden, viewModel.videoErrorUiState.first())
    }

    @Test
    fun `saveState should save current states`() = testCoroutineRule.runBlockingTest {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveUiState(outState)
        verify { outState.putParcelable(ReviewVideoPlayerViewModel.SAVED_STATE_PLAYBACK_UI_STATE, any()) }
        verify { outState.putParcelable(ReviewVideoPlayerViewModel.SAVED_STATE_VIDEO_PLAYER_UI_STATE, any()) }
    }

    @Test
    fun `restoreState should save current states`() = testCoroutineRule.runBlockingTest {
        val latestVideoPlaybackUiState = ReviewVideoPlaybackUiState.Playing(100L)
        val latestVideoPlayerUiState = ReviewVideoPlayerUiState.ReadyToPlay("https://tokopedia.com/patrickstarbellydancing.mp4")
        val savedState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState(ReviewVideoPlayerViewModel.SAVED_STATE_PLAYBACK_UI_STATE, viewModel.videoPlaybackUiState.value)
            } returns latestVideoPlaybackUiState
            every {
                getSavedState(ReviewVideoPlayerViewModel.SAVED_STATE_VIDEO_PLAYER_UI_STATE, viewModel.videoPlayerUiState.value)
            } returns latestVideoPlayerUiState
        }
        viewModel.restoreUiState(savedState)
        Assert.assertEquals(latestVideoPlaybackUiState, viewModel.videoPlaybackUiState.first())
        Assert.assertEquals(latestVideoPlayerUiState, viewModel.videoPlayerUiState.first())
    }
}