package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.google.android.exoplayer2.upstream.Loader.UnexpectedLoaderException
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.exception.PlayVideoErrorException
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.PlayPlayerModel
import com.tokopedia.play_common.player.state.ExoPlayerStateProcessorImpl
import com.tokopedia.play_common.state.PlayVideoPrepareState
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.state.VideoPositionHandle
import com.tokopedia.play_common.types.PlayVideoType
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayProcessLifecycleObserver
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.properties.Delegates

/**
 * Created by jegul on 03/12/19
 */
class PlayVideoManager private constructor(private val applicationContext: Context) {

    /**
     * VideoPlayer shared state
     */
    private var isMuted: Boolean = false
    private var isRepeated: Boolean = false

    private val exoPlaybackExceptionParser = ExoPlaybackExceptionParser()
    private var currentPrepareState: PlayVideoPrepareState = getDefaultPrepareState()

    @Deprecated(message = "Use listener instead")
    private val _observablePlayVideoState = MutableLiveData<PlayVideoState>()
    @Deprecated(message = "Use listener instead")
    private val _observableVideoPlayer = MutableLiveData<SimpleExoPlayer>()

    private val playerStateProcessor = ExoPlayerStateProcessorImpl()
    private val listeners: MutableList<Listener> = mutableListOf()

    private val listenerPlayerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            broadcastStateToListeners(safeProcessPlaybackState(playWhenReady, playbackState))
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            broadcastStateToListeners(PlayVideoState.Error(PlayVideoErrorException(error.cause)))
        }
    }

    private val liveDataPlayerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            _observablePlayVideoState.value = safeProcessPlaybackState(playWhenReady, playbackState)
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            _observablePlayVideoState.value = PlayVideoState.Error(PlayVideoErrorException(error.cause))
        }
    }

    private val playerEventListener = object : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            listenerPlayerEventListener.onPlayerStateChanged(playWhenReady, playbackState)
            liveDataPlayerEventListener.onPlayerStateChanged(playWhenReady, playbackState)
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val parsedException = exoPlaybackExceptionParser.parse(error)

            if (
                    parsedException.isBehindLiveWindowException ||
                    parsedException.isInvalidResponseCodeException
            ) {

                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    stop()
                    playUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            } else if (
                    parsedException.isUnknownHostException ||
                    parsedException.isConnectException
            ) {
                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    stop(resetState = false)
                    playUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            } else if (parsedException.isUnexpectedLoaderException) {
                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    release()
                    playUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            } else {
                //For now it's the same as the defined exception above
                val prepareState = currentPrepareState
                if (prepareState is PlayVideoPrepareState.Prepared) {
                    release()
                    playUri(prepareState.uri, videoPlayer.playWhenReady)
                }
            }

            listenerPlayerEventListener.onPlayerError(error)
            liveDataPlayerEventListener.onPlayerError(error)
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
        if (old.player != new.player) {
            broadcastVideoPlayerToListeners(new.player)
            _observableVideoPlayer.value = new.player
        }
    }

    val videoPlayer: SimpleExoPlayer
        get() = playerModel.player

    val currentUri: Uri?
        get() {
            return when (val currentState = currentPrepareState) {
                is PlayVideoPrepareState.Unprepared -> currentState.previousUri
                is PlayVideoPrepareState.Prepared -> currentState.uri
            }
        }

    //region public method
    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun playUri(uri: Uri, autoPlay: Boolean = true, bufferControl: PlayBufferControl = playerModel.loadControl.bufferControl) {
        if (uri.toString().isEmpty()) {
            release()
            return
        }

        val prepareState = currentPrepareState
        if (currentUri == null) playerModel = initVideoPlayer(playerModel, bufferControl)
        if (prepareState is PlayVideoPrepareState.Unprepared || currentUri != uri) {
            val lastPosition = if (prepareState is PlayVideoPrepareState.Unprepared && !prepareState.previousType.isLive && currentUri == uri) prepareState.lastPosition else null
            val resetState = if (prepareState is PlayVideoPrepareState.Unprepared && currentUri == uri) prepareState.resetState else true
            playVideoWithUri(uri, autoPlay, lastPosition, resetState)
            currentPrepareState = PlayVideoPrepareState.Prepared(uri, if (prepareState is PlayVideoPrepareState.Unprepared) VideoPositionHandle.NotHandled(lastPosition) else VideoPositionHandle.Handled)
        }
        if (!videoPlayer.isPlaying && autoPlay) resume()
    }

    private fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true, lastPosition: Long?, resetState: Boolean = true) {
        val mediaSource = getMediaSourceBySource(applicationContext, uri)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource, resetState, resetState)
        if (lastPosition == null) videoPlayer.seekToDefaultPosition()
    }

    //endregion

    //region player control
    fun resume() {
        playerModel.loadControl.setPreventLoading(false)
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) reset()
        videoPlayer.playWhenReady = true
    }

    fun pause(preventLoadingBuffer: Boolean) {
        playerModel.loadControl.setPreventLoading(preventLoadingBuffer)
        videoPlayer.playWhenReady = false
    }

    fun resumeOrPlayPreviousVideo(autoPlay: Boolean) {
        val prepareState = currentPrepareState
        if (prepareState is PlayVideoPrepareState.Unprepared && prepareState.previousUri != null) {
            playUri(prepareState.previousUri, autoPlay)
        } else if (prepareState is PlayVideoPrepareState.Prepared) resume()
    }

    fun reset() {
        videoPlayer.seekTo(0)
    }

    fun release() {
        currentPrepareState = getDefaultPrepareState()
        videoPlayer.release()
    }

    fun stop(resetState: Boolean = true) {
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
    @Deprecated(message = "Use listener instead")
    fun getObservablePlayVideoState(): LiveData<PlayVideoState> = _observablePlayVideoState
    @Deprecated(message = "Use listener instead")
    fun getObservableVideoPlayer(): LiveData<out ExoPlayer> = _observableVideoPlayer

    fun isPlaying(): Boolean = videoPlayer.isPlaying

    fun getVideoDuration(): Long {
        return videoPlayer.duration
    }

    fun getCurrentPosition(): Long = videoPlayer.currentPosition

    fun isVideoMuted(): Boolean {
        return isMuted
    }

    fun mute(shouldMute: Boolean) = synchronized(this) {
        mute(videoPlayer, shouldMute)
    }

    private fun mute(videoPlayer: SimpleExoPlayer, shouldMute: Boolean) = synchronized(this) {
        isMuted = shouldMute
        videoPlayer.volume = if (shouldMute) VIDEO_MIN_SOUND else VIDEO_MAX_SOUND
    }

    fun setRepeatMode(shouldRepeat: Boolean) = synchronized(this) {
        setRepeatMode(videoPlayer, shouldRepeat)
    }

    private fun setRepeatMode(videoPlayer: SimpleExoPlayer, shouldRepeat: Boolean) = synchronized(this) {
        isRepeated = shouldRepeat
        videoPlayer.repeatMode = if(shouldRepeat) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
    }

    fun isVideoRepeat(): Boolean {
        return isRepeated
    }

    fun isVideoLive(): Boolean = videoPlayer.isCurrentWindowLive

    fun getVideoState(): PlayVideoState = _observablePlayVideoState.value ?: PlayVideoState.NoMedia
    //endregion

    //region private method
    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"))
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

    private fun getErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return object : DefaultLoadErrorHandlingPolicy() {
            override fun getRetryDelayMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return if (exception is ParserException || exception is FileNotFoundException || exception is UnexpectedLoaderException) C.TIME_UNSET
                else errorCount * RETRY_DELAY
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return when (dataType) {
                    C.DATA_TYPE_MANIFEST -> 0
                    C.DATA_TYPE_MEDIA_PROGRESSIVE_LIVE -> RETRY_COUNT_LIVE
                    else -> RETRY_COUNT_DEFAULT
                }
            }

            override fun getBlacklistDurationMsFor(dataType: Int, loadDurationMs: Long, exception: IOException?, errorCount: Int): Long {
                return C.TIME_UNSET
            }
        }
    }

    private fun initVideoPlayer(playerModel: PlayPlayerModel?, bufferControl: PlayBufferControl): PlayPlayerModel {
        playerModel?.player?.removeListener(playerEventListener)
        val videoLoadControl = initCustomLoadControl(bufferControl)
        val videoPlayer = SimpleExoPlayer.Builder(applicationContext)
                .setLoadControl(videoLoadControl)
                .build()
                .apply {
                    addListener(playerEventListener)
                    setAudioAttributes(initAudioAttributes(), true)
                }
                .also {
                    mute(it, isMuted)
                    setRepeatMode(it, isRepeated)
                }

        return PlayPlayerModel(videoPlayer, videoLoadControl)
    }

    private fun safeProcessPlaybackState(playWhenReady: Boolean, playbackState: Int): PlayVideoState {
        return try {
            playerStateProcessor.processState(playWhenReady, playbackState)
        } catch (e: IllegalStateException) {
            PlayVideoState.Error(e)
        }
    }

    private fun broadcastStateToListeners(state: PlayVideoState) {
        listeners.forEach { it.onPlayerStateChanged(state) }
    }

    private fun broadcastVideoPlayerToListeners(exoPlayer: ExoPlayer) {
        listeners.forEach { it.onVideoPlayerChanged(exoPlayer) }
    }

    /**
     * To handle audio focus and content type
     */
    private fun initAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .setUsage(C.USAGE_MEDIA)
                .build()
    }

    private fun initCustomLoadControl(bufferControl: PlayBufferControl): PlayVideoLoadControl {
        return PlayVideoLoadControl(bufferControl)
    }

    companion object {
        private const val RETRY_COUNT_LIVE = 1
        private const val RETRY_COUNT_DEFAULT = 2
        private const val RETRY_DELAY = 2000L
        private const val BLACKLIST_MS = 10000L

        private const val VIDEO_MAX_SOUND = 1f
        private const val VIDEO_MIN_SOUND = 0f

        @Volatile
        private var INSTANCE: PlayVideoManager? = null

        private var playProcessLifecycleObserver: PlayProcessLifecycleObserver? = null

        @JvmStatic
        fun getInstance(context: Context): PlayVideoManager {
            return INSTANCE ?: synchronized(this) {
                val player = PlayVideoManager(context.applicationContext).also {
                    INSTANCE = it
                }

                if (playProcessLifecycleObserver == null)
                    playProcessLifecycleObserver = PlayProcessLifecycleObserver(context.applicationContext)

                playProcessLifecycleObserver?.let { ProcessLifecycleOwner.get()
                        .lifecycle.addObserver(it) }

                player
            }
        }

        @JvmStatic
        fun deleteInstance() = synchronized(this) {
            if (INSTANCE != null) {
                playProcessLifecycleObserver?.let { ProcessLifecycleOwner.get()
                        .lifecycle.removeObserver(it) }

                INSTANCE!!.videoPlayer.removeListener(INSTANCE!!.playerEventListener)
                INSTANCE = null
            }
        }
    }

    interface Listener {

        fun onPlayerStateChanged(state: PlayVideoState) {}
        fun onVideoPlayerChanged(player: ExoPlayer) {}
    }
}