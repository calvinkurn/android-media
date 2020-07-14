package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader.UnexpectedLoaderException
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.exception.PlayVideoErrorException
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.PlayPlayerModel
import com.tokopedia.play_common.state.PlayVideoPrepareState
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.state.VideoPositionHandle
import com.tokopedia.play_common.types.PlayVideoType
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.properties.Delegates

/**
 * Created by jegul on 03/12/19
 */
class PlayVideoManager private constructor(private val applicationContext: Context) {

    companion object {
        private const val RETRY_COUNT_LIVE = 1
        private const val RETRY_COUNT_DEFAULT = 2
        private const val RETRY_DELAY = 2000L

        private const val VIDEO_MAX_SOUND = 1f
        private const val VIDEO_MIN_SOUND = 0f

        @Volatile
        private var INSTANCE: PlayVideoManager? = null

        @JvmStatic
        fun getInstance(context: Context): PlayVideoManager {
            return INSTANCE ?: synchronized(this) {
                PlayVideoManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        @JvmStatic
        fun deleteInstance() = synchronized(this) {
            if (INSTANCE != null) {
                INSTANCE!!.videoPlayer.removeListener(INSTANCE!!.playerEventListener)
                INSTANCE = null
            }
        }
    }

    private val exoPlaybackExceptionParser = ExoPlaybackExceptionParser()
    private var currentPrepareState: PlayVideoPrepareState = getDefaultPrepareState()
    private val _observablePlayVideoState = MutableLiveData<PlayVideoState>()
    private val _observableVideoPlayer = MutableLiveData<SimpleExoPlayer>()

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> _observablePlayVideoState.value = PlayVideoState.NoMedia
                Player.STATE_BUFFERING -> _observablePlayVideoState.value = PlayVideoState.Buffering
                Player.STATE_READY -> {
                    _observablePlayVideoState.value = if (!playWhenReady) PlayVideoState.Pause else PlayVideoState.Playing
                }
                Player.STATE_ENDED -> _observablePlayVideoState.value = PlayVideoState.Ended
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val parsedException = exoPlaybackExceptionParser.parse(error)
//            if (parsedException.isBlackListedException) {
//                stopPlayer()
//                return
//            }

            if (
                    parsedException.isBehindLiveWindowException ||
                    parsedException.isInvalidResponseCodeException
            ) {

                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    stopPlayer()
                    safePlayVideoWithUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            } else if (
                    parsedException.isUnknownHostException ||
                    parsedException.isConnectException
            ) {
                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    stopPlayer(resetState = false)
                    safePlayVideoWithUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            }
            else {
                //For now it's the same as the defined exception above
                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    stopPlayer()
                    safePlayVideoWithUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            }
            _observablePlayVideoState.value = PlayVideoState.Error(PlayVideoErrorException(error.cause))
        }

        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            try {
                if (!timeline.isEmpty) {
                    val currentWindow = timeline.getWindow(0, Timeline.Window())
                    val prepareState = currentPrepareState
                    if (!currentWindow.isLive && !currentWindow.isDynamic && prepareState is PlayVideoPrepareState.Prepared && prepareState.positionHandle is VideoPositionHandle.NotHandled) {
                        currentPrepareState = prepareState.copy(positionHandle = VideoPositionHandle.Handled)
                        val lastPosition = prepareState.positionHandle.lastPosition
                        if (lastPosition != null && currentWindow.durationMs >= lastPosition) videoPlayer.seekTo(lastPosition)
                    }
                }
            } catch (e: Exception) {}
        }
    }

    private var playerModel: PlayPlayerModel by Delegates.observable(initVideoPlayer(null, PlayBufferControl())) { _, old, new ->
        if (old.player != new.player) _observableVideoPlayer.value = new.player
    }

    val videoPlayer: SimpleExoPlayer
        get() = playerModel.player

    //region public method
    fun safePlayVideoWithUri(uri: Uri, autoPlay: Boolean = true, bufferControl: PlayBufferControl = playerModel.loadControl.bufferControl) {
        if (uri.toString().isEmpty()) {
            releasePlayer()
            return
        }

        val prepareState = currentPrepareState
        val currentUri: Uri? = when (prepareState) {
            is PlayVideoPrepareState.Unprepared -> prepareState.previousUri
            is PlayVideoPrepareState.Prepared -> prepareState.uri
        }
        if (currentUri == null) playerModel = initVideoPlayer(playerModel, bufferControl)
        if (prepareState is PlayVideoPrepareState.Unprepared || currentUri != uri) {
            val lastPosition = if (prepareState is PlayVideoPrepareState.Unprepared && !prepareState.previousType.isLive && currentUri == uri) prepareState.lastPosition else null
            val resetState = if (prepareState is PlayVideoPrepareState.Unprepared && currentUri == uri) prepareState.resetState else true
            playVideoWithUri(uri, autoPlay, lastPosition, resetState)
            currentPrepareState = PlayVideoPrepareState.Prepared(uri, if (prepareState is PlayVideoPrepareState.Unprepared) VideoPositionHandle.NotHandled(lastPosition) else VideoPositionHandle.Handled)
        }
        if (!videoPlayer.isPlaying) resumeCurrentVideo()
    }

    private fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true, lastPosition: Long?, resetState: Boolean = true) {
        val mediaSource = getMediaSourceBySource(applicationContext, uri)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource, resetState, resetState)
        if (lastPosition == null) videoPlayer.seekToDefaultPosition()
    }

    //endregion

    //region player control
    fun resumeCurrentVideo() {
        playerModel.loadControl.setPreventLoading(false)
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) resetCurrentVideo()
        videoPlayer.playWhenReady = true
    }

    fun pauseCurrentVideo(preventLoadingBuffer: Boolean) {
        playerModel.loadControl.setPreventLoading(preventLoadingBuffer)
        videoPlayer.playWhenReady = false
    }

    fun resumeOrPlayPreviousVideo() {
        val prepareState = currentPrepareState
        if (prepareState is PlayVideoPrepareState.Unprepared && prepareState.previousUri != null) {
            safePlayVideoWithUri(prepareState.previousUri)
        }
    }

    fun resetCurrentVideo() {
        videoPlayer.seekTo(0)
    }

    fun releasePlayer() {
        currentPrepareState = getDefaultPrepareState()
        videoPlayer.release()
    }

    fun stopPlayer(resetState: Boolean = true) {
        val prepareState = currentPrepareState
        if (prepareState is PlayVideoPrepareState.Prepared)
            currentPrepareState = PlayVideoPrepareState.Unprepared(
                    previousUri = prepareState.uri,
                    previousType = if (isVideoLive()) PlayVideoType.Live else PlayVideoType.VOD,
                    lastPosition = when (prepareState.positionHandle) {
                        VideoPositionHandle.Handled -> getCurrentPosition()
                        is VideoPositionHandle.NotHandled -> prepareState.positionHandle.lastPosition
                    },
                    resetState = resetState
            )

        videoPlayer.stop()
    }

    private fun getDefaultPrepareState() = PlayVideoPrepareState.Unprepared(
            previousUri = null,
            previousType = PlayVideoType.Unknown,
            lastPosition = null,
            resetState = true
    )
    //endregion

    //region video state
    fun getObservablePlayVideoState(): LiveData<PlayVideoState> = _observablePlayVideoState
    fun getObservableVideoPlayer(): LiveData<out ExoPlayer> = _observableVideoPlayer

    fun isVideoPlaying(): Boolean = videoPlayer.isPlaying

    fun getDurationVideo(): Long {
        return videoPlayer.duration
    }

    fun getCurrentPosition(): Long = videoPlayer.currentPosition

    fun isVideoMuted(): Boolean {
        return videoPlayer.volume == VIDEO_MIN_SOUND
    }

    fun muteVideo(shouldMute: Boolean) {
        videoPlayer.volume = if (shouldMute) VIDEO_MIN_SOUND else VIDEO_MAX_SOUND
    }

    fun setRepeatMode(shouldRepeat: Boolean) {
        videoPlayer.repeatMode = if(shouldRepeat) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
    }

    fun isVideoRepeat(): Boolean {
        return videoPlayer.repeatMode != Player.REPEAT_MODE_OFF
    }

    fun isVideoLive(): Boolean = videoPlayer.isCurrentWindowLive
    //endregion

    //region private method
    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Play"))
        val errorHandlingPolicy = getErrorHandlingPolicy()
        val mediaSource = when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory).setLoadErrorHandlingPolicy(errorHandlingPolicy)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
        return mediaSource.createMediaSource(uri)
    }
    //endregion

    private fun getErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return object : DefaultLoadErrorHandlingPolicy() {
            override fun getRetryDelayMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return if (exception is ParserException || exception is FileNotFoundException || exception is UnexpectedLoaderException) C.TIME_UNSET
                else (errorCount * RETRY_DELAY) + RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return if (dataType == C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE) RETRY_COUNT_LIVE else RETRY_COUNT_DEFAULT
            }
        }
    }

    private fun initVideoPlayer(playerModel: PlayPlayerModel?, bufferControl: PlayBufferControl): PlayPlayerModel {
        playerModel?.player?.removeListener(playerEventListener)
        val videoLoadControl = initCustomLoadControl(bufferControl)
        val videoPlayer = SimpleExoPlayer.Builder(applicationContext)
                .setLoadControl(videoLoadControl)
                .build()
                .apply { addListener(playerEventListener) }

        return PlayPlayerModel(videoPlayer, videoLoadControl)
    }

    private fun initCustomLoadControl(bufferControl: PlayBufferControl): PlayVideoLoadControl {
        return PlayVideoLoadControl(bufferControl)
    }
}