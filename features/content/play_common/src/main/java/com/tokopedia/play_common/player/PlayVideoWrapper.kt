package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.PlayPlayerModel
import com.tokopedia.play_common.player.creator.DefaultExoPlayerCreator
import com.tokopedia.play_common.player.creator.ExoPlayerCreator
import com.tokopedia.play_common.player.errorhandlingpolicy.PlayLoadErrorHandlingPolicy
import com.tokopedia.play_common.player.state.ExoPlayerStateProcessorImpl
import com.tokopedia.play_common.state.PlayVideoPrepareState
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.state.VideoPositionHandle
import com.tokopedia.play_common.types.PlayVideoType
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.properties.Delegates

/**
 * Created by jegul on 27/01/21
 */
class PlayVideoWrapper private constructor(
        private val context: Context,
        private val exoPlayerCreator: ExoPlayerCreator = DefaultExoPlayerCreator(context)
) {

    class Builder(
            context: Context
    ) {

        private val mContext = context.applicationContext

        private var mExoPlayerCreator: ExoPlayerCreator? = null

        fun setExoPlayerCreator(exoPlayerCreator: ExoPlayerCreator): Builder {
            mExoPlayerCreator = exoPlayerCreator
            return this
        }

        fun build(): PlayVideoWrapper {
            return PlayVideoWrapper(mContext, mExoPlayerCreator ?: DefaultExoPlayerCreator(mContext))
        }
    }

    /**
     * VideoPlayer state
     */
    private var isMuted: Boolean = false
    private var isRepeated: Boolean = false

    private val exoPlaybackExceptionParser = ExoPlaybackExceptionParser()
    private var currentPrepareState: PlayVideoPrepareState = getDefaultPrepareState()

    private val playerStateProcessor = ExoPlayerStateProcessorImpl()
    private val listeners: ConcurrentLinkedQueue<Listener> = ConcurrentLinkedQueue()

    private val playerEventListener = object : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            broadcastStateToListeners(safeProcessPlaybackState(playWhenReady, playbackState))
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

            broadcastStateToListeners(PlayVideoState.Error(error))
        }

        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            try {
                if (!timeline.isEmpty) {
                    val currentWindow = timeline.getWindow(0, Timeline.Window())
                    val prepareState = currentPrepareState
                    if (!currentWindow.isLive && !currentWindow.isDynamic && prepareState is PlayVideoPrepareState.Prepared && prepareState.positionHandle is VideoPositionHandle.NotHandled) {
                        val lastPosition = prepareState.positionHandle.lastPosition
                        if (lastPosition != null && currentWindow.durationMs >= lastPosition) videoPlayer.seekTo(lastPosition)
                        currentPrepareState = prepareState.copy(positionHandle = VideoPositionHandle.Handled)
                    }
                }
            } catch (e: Exception) {}
        }
    }

    private var playerModel: PlayPlayerModel by Delegates.observable(initVideoPlayer(null, PlayBufferControl())) { _, old, new ->
        if (old.player != new.player) {
            broadcastVideoPlayerToListeners(new.player)
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
    fun getVideoStateFlow() = callbackFlow<PlayVideoState> {
        val listener = object : Listener {
            override fun onPlayerStateChanged(state: PlayVideoState) {
                try { offer(state) } catch (e: Throwable) {}
            }
        }

        addListener(listener)

        awaitClose { removeListener(listener) }
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun playUri(uri: Uri, autoPlay: Boolean = true, bufferControl: PlayBufferControl = playerModel.loadControl.bufferControl, startPosition: Long? = null) {
        if (uri.toString().isEmpty()) {
            release()
            return
        }

        val prepareState = currentPrepareState
        if (currentUri == null) playerModel = initVideoPlayer(playerModel, bufferControl)
        if (prepareState is PlayVideoPrepareState.Unprepared || currentUri != uri) {

            val lastPosition = startPosition ?: if (prepareState is PlayVideoPrepareState.Unprepared && !prepareState.previousType.isLive && currentUri == uri) prepareState.lastPosition else null

            val resetState = if (prepareState is PlayVideoPrepareState.Unprepared && currentUri == uri) prepareState.resetState else true
            playVideoWithUri(uri, autoPlay, lastPosition, resetState)
            currentPrepareState = PlayVideoPrepareState.Prepared(uri, if (prepareState is PlayVideoPrepareState.Unprepared) VideoPositionHandle.NotHandled(lastPosition) else VideoPositionHandle.Handled)
        }
        if (!videoPlayer.isPlaying && autoPlay) resume()
    }

    private fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true, lastPosition: Long?, resetState: Boolean = true) {
        val mediaSource = getMediaSourceBySource(context, uri)
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
    //endregion

    private fun getDefaultPrepareState() = PlayVideoPrepareState.Unprepared(
            previousUri = null,
            previousType = PlayVideoType.Unknown,
            lastPosition = null,
            resetState = true
    )

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
    //endregion

    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var transferredData: Long = 0L

    private val transferListener = object : TransferListener {
        override fun onTransferInitializing(dataSource: DataSource?, dataSpec: DataSpec?, isNetwork: Boolean) {}

        override fun onTransferStart(dataSource: DataSource?, dataSpec: DataSpec?, isNetwork: Boolean) {
            startTime = System.currentTimeMillis()
        }

        override fun onBytesTransferred(dataSource: DataSource?, dataSpec: DataSpec?, isNetwork: Boolean, bytes: Int) {
            transferredData = bytes * BYTES_MULTIPLIER
        }

        override fun onTransferEnd(dataSource: DataSource?, dataSpec: DataSpec?, isNetwork: Boolean) {
            endTime = System.currentTimeMillis()
            PlayLiveRoomMetricsCommon.setInetSpeed(getDownstreamBandwidth().toInt())
        }
    }

    private fun getMediaSourceBySource(context: Context, uri: Uri): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Tokopedia Android"), transferListener)
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

    fun getDownstreamBandwidth(): Long {
        val convertedTransferredData = transferredData / MBPS_DIVIDER
        val timeOffset = endTime - startTime
        return if(timeOffset > 0) convertedTransferredData / timeOffset else 0
    }

    private fun getErrorHandlingPolicy(): LoadErrorHandlingPolicy {
        return PlayLoadErrorHandlingPolicy()
    }

    private fun initVideoPlayer(playerModel: PlayPlayerModel?, bufferControl: PlayBufferControl): PlayPlayerModel {
        playerModel?.player?.removeListener(playerEventListener)
        val videoLoadControl = initCustomLoadControl(bufferControl)
        val videoPlayer = exoPlayerCreator.createExoPlayer(videoLoadControl)
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
        private const val VIDEO_MAX_SOUND = 1f
        private const val VIDEO_MIN_SOUND = 0f

        private const val MBPS_DIVIDER = 1000000L
        private const val BYTES_MULTIPLIER = 8000L
    }

    interface Listener {

        fun onPlayerStateChanged(state: PlayVideoState) {}
        fun onVideoPlayerChanged(player: ExoPlayer) {}
    }
}