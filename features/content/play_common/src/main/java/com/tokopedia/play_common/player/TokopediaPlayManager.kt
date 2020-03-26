package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader.UnexpectedLoaderException
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.exception.PlayVideoErrorException
import com.tokopedia.play_common.state.TokopediaPlayPrepareState
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.play_common.state.VideoPositionHandle
import com.tokopedia.play_common.types.TokopediaPlayVideoType
import com.tokopedia.play_common.util.ExoPlaybackExceptionHelper
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.properties.Delegates

/**
 * Created by jegul on 03/12/19
 */
class TokopediaPlayManager private constructor(private val applicationContext: Context) {

    companion object {
        private const val RETRY_COUNT_LIVE = 1
        private const val RETRY_COUNT_DEFAULT = 2
        private const val RETRY_DELAY = 1000L

        private const val VIDEO_MAX_SOUND = 1f
        private const val VIDEO_MIN_SOUND = 0f

        @Volatile
        private var INSTANCE: TokopediaPlayManager? = null

        @JvmStatic
        fun getInstance(context: Context): TokopediaPlayManager {
            return INSTANCE ?: synchronized(this) {
                TokopediaPlayManager(context.applicationContext).also {
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

    private var currentPrepareState: TokopediaPlayPrepareState = TokopediaPlayPrepareState.Unprepared(null, TokopediaPlayVideoType.Unknown, null)
    private val _observablePlayVideoState = MutableLiveData<TokopediaPlayVideoState>()
    private val _observableVideoPlayer = MutableLiveData<SimpleExoPlayer>()

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (!playWhenReady) _observablePlayVideoState.value = TokopediaPlayVideoState.Pause
            else when (playbackState) {
                Player.STATE_IDLE -> _observablePlayVideoState.value = TokopediaPlayVideoState.NoMedia
                Player.STATE_BUFFERING -> _observablePlayVideoState.value = TokopediaPlayVideoState.Buffering
                Player.STATE_READY -> _observablePlayVideoState.value = TokopediaPlayVideoState.Playing
                Player.STATE_ENDED -> _observablePlayVideoState.value = TokopediaPlayVideoState.Ended
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            if (ExoPlaybackExceptionHelper.isBehindLiveWindow(error) ||
                    ExoPlaybackExceptionHelper.isInvalidResponseCode(error) ||
                    ExoPlaybackExceptionHelper.isConnectException(error)) {

                val prepareState = currentPrepareState
                if (prepareState is TokopediaPlayPrepareState.Prepared) {
                    stopPlayer()
                    safePlayVideoWithUri(prepareState.uri, videoPlayer.playWhenReady, true)
                }
            } else {
                //For now it's the same as the defined exception above
                val prepareState = currentPrepareState
                if (prepareState is TokopediaPlayPrepareState.Prepared) {
                    stopPlayer()
                    safePlayVideoWithUri(prepareState.uri, videoPlayer.playWhenReady, true)
                }
            }
            _observablePlayVideoState.value = TokopediaPlayVideoState.Error(PlayVideoErrorException(error.cause))
        }

        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            try {
                if (!timeline.isEmpty) {
                    val currentWindow = timeline.getWindow(0, Timeline.Window())
                    val prepareState = currentPrepareState
                    if (!currentWindow.isLive && !currentWindow.isDynamic && prepareState is TokopediaPlayPrepareState.Prepared && prepareState.positionHandle is VideoPositionHandle.NotHandled) {
                        currentPrepareState = prepareState.copy(positionHandle = VideoPositionHandle.Handled)
                        if (prepareState.positionHandle.lastPosition != null) videoPlayer.seekTo(prepareState.positionHandle.lastPosition)
                    }
                }
            } catch (e: Exception) {}
        }
    }

    var videoPlayer: SimpleExoPlayer by Delegates.observable(initVideoPlayer(null)) { _, _, _ ->
        _observableVideoPlayer.value = videoPlayer
    }

    //region public method
    fun safePlayVideoWithUri(uri: Uri, autoPlay: Boolean = true, forceReset: Boolean = false) {
        if (uri.toString().isEmpty()) {
            releasePlayer()
            return
        }

        val prepareState = currentPrepareState
        val currentUri: Uri? = when (prepareState) {
            is TokopediaPlayPrepareState.Unprepared -> prepareState.previousUri
            is TokopediaPlayPrepareState.Prepared -> prepareState.uri
        }
        if (currentUri == null) videoPlayer = initVideoPlayer(videoPlayer)
        if (prepareState is TokopediaPlayPrepareState.Unprepared || currentUri != uri) {
            val lastPosition = if (prepareState is TokopediaPlayPrepareState.Unprepared && !prepareState.previousType.isLive && currentUri == uri) prepareState.lastPosition else null
            playVideoWithUri(uri, autoPlay, lastPosition)
            currentPrepareState = TokopediaPlayPrepareState.Prepared(uri, if (prepareState is TokopediaPlayPrepareState.Unprepared) VideoPositionHandle.NotHandled(lastPosition) else VideoPositionHandle.Handled)
        }
        if (!videoPlayer.isPlaying) resumeCurrentVideo()
    }

    fun safePlayVideoWithUriString(uriString: String?, autoPlay: Boolean = true, forceReset: Boolean = false) {
        if (uriString.isNullOrEmpty()) {
            releasePlayer()
            return
        }

        safePlayVideoWithUri(Uri.parse(uriString), autoPlay, forceReset)
    }

    private fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true, lastPosition: Long?) {
        val mediaSource = getMediaSourceBySource(applicationContext, uri)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource, true, true)
        if (lastPosition == null) videoPlayer.seekToDefaultPosition()
    }

    //endregion

    //region player control
    fun resumeCurrentVideo() {
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) resetCurrentVideo()
        videoPlayer.playWhenReady = true
    }

    fun pauseCurrentVideo() {
        videoPlayer.playWhenReady = false
    }

    fun resetCurrentVideo() {
        videoPlayer.seekTo(0)
    }

    fun releasePlayer() {
        currentPrepareState = TokopediaPlayPrepareState.Unprepared(null, TokopediaPlayVideoType.Unknown, null)
        videoPlayer.release()
    }

    fun stopPlayer() {
        val prepareState = currentPrepareState
        if (prepareState is TokopediaPlayPrepareState.Prepared)
            currentPrepareState = TokopediaPlayPrepareState.Unprepared(
                    prepareState.uri,
                    if (isVideoLive()) TokopediaPlayVideoType.Live else TokopediaPlayVideoType.VOD,
                    getCurrentPosition()
            )

        videoPlayer.stop()
    }
    //endregion

    //region video state
    fun getObservablePlayVideoState(): LiveData<TokopediaPlayVideoState> = _observablePlayVideoState
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
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "home"))
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

            override fun getBlacklistDurationMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return C.TIME_UNSET
            }
        }
    }

    private fun initVideoPlayer(videoPlayer: SimpleExoPlayer?): SimpleExoPlayer {
        videoPlayer?.removeListener(playerEventListener)
        return SimpleExoPlayer.Builder(applicationContext)
                .build()
                .apply { addListener(playerEventListener) }
    }
}