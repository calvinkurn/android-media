package com.tokopedia.play

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.exoplayer.TestExoPlayer
import com.tokopedia.play.exoplayer.TestExoPlayerCreator
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.util.video.state.PlayViewerVideoStateListener
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

/**
 * Created by jegul on 15/09/20
 */
class PlayViewerVideoStateProcessorTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().context.applicationContext

    private val testExoPlayerCreator = TestExoPlayerCreator(appContext)
    private val playVideoManager = PlayVideoManager.getInstance(appContext, testExoPlayerCreator)
    private val playbackExceptionParser = ExoPlaybackExceptionParser()
    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcher = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher
            get() = testDispatcher
        override val immediate: CoroutineDispatcher
            get() = testDispatcher
        override val io: CoroutineDispatcher
            get() = testDispatcher
        override val computation: CoroutineDispatcher
            get() = testDispatcher
        override val default: CoroutineDispatcher
            get() = testDispatcher
    }

    private val scope = TestCoroutineScope(testDispatcher)

    private val playVideoProcessor = PlayViewerVideoStateProcessor.Factory(
            exoPlaybackExceptionParser = playbackExceptionParser,
            dispatcher = dispatcher
    ).create(
            scope = scope,
            channelTypeSource = { PlayChannelType.Live },
            playVideoPlayer = PlayVideoWrapper.Builder(appContext).build()
    )

    private var theState: PlayViewerVideoState = PlayViewerVideoState.Unknown
    private val listener = object : PlayViewerVideoStateListener {
        override fun onStateChanged(state: PlayViewerVideoState) {
            theState = state
        }
    }

    private val invalidResponseCodeException = HttpDataSource.InvalidResponseCodeException(
            HttpURLConnection.HTTP_NOT_FOUND,
            "",
            emptyMap<String, List<String>>(),
            DataSpec(Uri.EMPTY)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        playVideoProcessor.addStateListener(listener)
    }

    /**
     * PlayViewerVideoState
     * - Play
     * - Pause
     * - End
     * - Waiting
     * - Unknown
     * - Buffer
     * - Error
     */
    @Test
    fun whenPlayerStateReadyAndAutoPlay_thenViewerStateShouldBePlaying() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        exoPlayer.setState(true, Player.STATE_READY)
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Play)
    }

    @Test
    fun whenPlayerStateReadyAndNotAutoPlay_thenViewerStateShouldBePaused() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        exoPlayer.setState(false, Player.STATE_READY)
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Pause)
    }

    @Test
    fun whenPlayerHasEnded_thenViewerStateShouldBeEnded_regardlessOfAutoPlayState() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer

        exoPlayer.setState(false, Player.STATE_ENDED)
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.End)

        exoPlayer.setState(true, Player.STATE_ENDED)
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.End)
    }

    @Test
    fun whenPlayerIsBufferingForFirstTimeForAtLeastPredefinedOrMoreSeconds_thenViewerStateShouldBeWaiting() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        exoPlayer.setState(false, Player.STATE_BUFFERING)
        testDispatcher.advanceUntilIdle()
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Waiting)
    }

    @Test
    fun whenPlayerIsError_thenViewerStateShouldBeError() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        val error = ExoPlaybackException.createForUnexpected(RuntimeException())
        exoPlayer.setError(error)
        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Error(error))
    }

    @Test
    fun whenPlayerIsErrorFromBroadcaster_andThenGoesToBufferingState_thenViewerStateShouldBeWaiting() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        val error = ExoPlaybackException.createForSource(invalidResponseCodeException)
        exoPlayer.setError(error)

        exoPlayer.setState(false, Player.STATE_BUFFERING)
        testDispatcher.advanceUntilIdle()

        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Waiting)
    }

    @Test
    fun whenPlayerIsErrorFromViewer_andThenGoesToBufferingState_thenViewerStateShouldBeBufferingViewer() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        val error = ExoPlaybackException.createForUnexpected(RuntimeException())
        exoPlayer.setError(error)

        exoPlayer.setState(false, Player.STATE_BUFFERING)
        testDispatcher.advanceUntilIdle()

        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Buffer(BufferSource.Viewer))
    }

    @Test
    fun whenPlayerHasBeenPlaying_thenPlayerIsErrorFromBroadcaster_andThenGoesToBufferingState_thenViewerStateShouldBeBufferingBroadcaster() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        exoPlayer.setState(true, Player.STATE_READY)

        val error = ExoPlaybackException.createForSource(invalidResponseCodeException)
        exoPlayer.setError(error)

        exoPlayer.setState(false, Player.STATE_BUFFERING)
        testDispatcher.advanceUntilIdle()

        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Buffer(BufferSource.Broadcaster))
    }

    @Test
    fun whenPlayerIsBufferingForFirstTimeForLessThanPredefinedSeconds_andThenPlayerIsPlaying_thenViewerStateShouldBePlayingWithoutEverWaiting() = runBlockingTest {
        val exoPlayer = playVideoManager.videoPlayer as TestExoPlayer
        exoPlayer.setState(false, Player.STATE_BUFFERING)

        //If still less than predefined seconds, should still be unknown
        Assertions.assertThat(theState)
                .isNotEqualTo(PlayViewerVideoState.Waiting)

        exoPlayer.setState(true, Player.STATE_READY)

        Assertions.assertThat(theState)
                .isEqualTo(PlayViewerVideoState.Play)
    }

    @After
    fun tearDown() {
        playVideoProcessor.removeStateListener(listener)
        Dispatchers.resetMain()
    }
}