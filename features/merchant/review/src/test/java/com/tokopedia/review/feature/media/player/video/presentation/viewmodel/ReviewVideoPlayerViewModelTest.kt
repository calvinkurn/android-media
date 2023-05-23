package com.tokopedia.review.feature.media.player.video.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoErrorUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoPlaybackUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoPlayerUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoThumbnailUiState
import com.tokopedia.reviewcommon.extension.getSavedState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ReviewVideoPlayerViewModelTest: ReviewVideoPlayerViewModelTestFixture() {
    @Test
    fun `setVideoUri should update _videoPlayerUiState to ReviewVideoPlayerUiState#Initial`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.Initial)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToChangingConfiguration should update _videoPlayerUiState to ReviewVideoPlayerUiState#ChangingConfiguration`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToChangingConfiguration()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.ChangingConfiguration)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `resetVideoPlayerState should update _videoPlayerUiState to ReviewVideoPlayerUiState#Initial`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.resetVideoPlayerState()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.Initial)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToRestoring should update _videoPlayerUiState to ReviewVideoPlayerUiState#RestoringState`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.RestoringState)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setVideoPlayerStateToReadyToPlay should update _videoPlayerUiState to ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        val currentVideoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(currentVideoPlayerUiState is ReviewVideoPlayerUiState.ReadyToPlay)
        Assert.assertEquals(newVideoUri, currentVideoPlayerUiState.videoUri)
    }

    @Test
    fun `setPlaybackStateToPlaying should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Playing when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPlaying should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPlaying should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPlaying(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToBuffering should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Buffering when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToBuffering should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToBuffering should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToBuffering(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToPaused should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Paused when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPaused should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPaused should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPaused(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToPreloading should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Preloading when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPreloading should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToPreloading should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToPreloading(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToEnded should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Ended when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToEnded should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToEnded should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToEnded(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToError should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Error when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToError(newPosition, "")
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        Assert.assertTrue(currentVideoPlaybackUiState is ReviewVideoPlaybackUiState.Error)
        Assert.assertEquals(newPosition, currentVideoPlaybackUiState.currentPosition)
    }

    @Test
    fun `setPlaybackStateToError should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        val previousVideoPlaybackUiState = viewModel.videoPlaybackUiState.first()
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToRestoring()
        viewModel.setPlaybackStateToError(newPosition, "")
        val currentVideoPlaybackUiState = viewModel.videoPlaybackUiState.value
        Assert.assertEquals(previousVideoPlaybackUiState, currentVideoPlaybackUiState)
    }

    @Test
    fun `setPlaybackStateToError should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Hidden`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToError(newPosition, "")
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Hidden, videoThumbnailUiState)
    }

    @Test
    fun `setPlaybackStateToInactive should update _videoPlaybackUiState to ReviewVideoPlaybackUiState#Inactive when current _videoPlayerUiState value is ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToInactive should not update _videoPlaybackUiState when current _videoPlayerUiState value is not ReviewVideoPlayerUiState#ReadyToPlay`() = testCoroutineRule.runTest {
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
    fun `setPlaybackStateToInactive should update videoThumbnailUiState to ReviewVideoThumbnailUiState#Showed`() = testCoroutineRule.runTest {
        val newVideoUri = "https://tokopedia.com/patrickstarbellydancing.mp4"
        val newPosition = 100L
        viewModel.setVideoUri(newVideoUri)
        viewModel.setVideoPlayerStateToReadyToPlay()
        viewModel.setPlaybackStateToInactive(newPosition)
        val videoThumbnailUiState = viewModel.videoThumbnailUiState.first()
        Assert.assertEquals(ReviewVideoThumbnailUiState.Showed, videoThumbnailUiState)
    }

    @Test
    fun `showVideoError should update _videoErrorUiState to ReviewVideoErrorUiState#Showing`() = testCoroutineRule.runTest {
        viewModel.showVideoError("")
        Assert.assertEquals(ReviewVideoErrorUiState.Showing(""), viewModel.videoErrorUiState.first())
    }

    @Test
    fun `hideVideoError should update _videoErrorUiState to ReviewVideoErrorUiState#Hidden`() = testCoroutineRule.runTest {
        viewModel.hideVideoError()
        Assert.assertEquals(ReviewVideoErrorUiState.Hidden, viewModel.videoErrorUiState.first())
    }

    @Test
    fun `saveState should save current states`() = testCoroutineRule.runTest {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveUiState(outState)
        verify { outState.putParcelable(ReviewVideoPlayerViewModel.SAVED_STATE_PLAYBACK_UI_STATE, any()) }
        verify { outState.putParcelable(ReviewVideoPlayerViewModel.SAVED_STATE_VIDEO_PLAYER_UI_STATE, any()) }
    }

    @Test
    fun `restoreState should save current states`() = testCoroutineRule.runTest {
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

    @Test
    fun `updateWifiConnectivityStatus should make videoPlayerUiState autoplay`() = runTest {
        viewModel.setVideoPlayerStateToRestoring()
        var videoPlayerUiState = viewModel.videoPlayerUiState.first()
        viewModel.updateWifiConnectivityStatus(true)
        Assert.assertTrue(videoPlayerUiState is ReviewVideoPlayerUiState.RestoringState && !videoPlayerUiState.playWhenReady)
        videoPlayerUiState = viewModel.videoPlayerUiState.first()
        Assert.assertTrue(videoPlayerUiState is ReviewVideoPlayerUiState.RestoringState && videoPlayerUiState.playWhenReady)
    }

    @Test
    fun `getImpressHolder should return ImpressHolder`() {
        Assert.assertTrue(viewModel.getImpressHolder() is ImpressHolder)
    }
}
