package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tokopedia.play_common.exception.PlayVideoErrorException
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.play_common.type.Http
import com.tokopedia.play_common.type.Rtmp
import com.tokopedia.play_common.type.PlayVideoProtocol

/**
 * Created by jegul on 03/12/19
 */
class TokopediaPlayManager private constructor(private val applicationContext: Context) {

    companion object {
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"

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

    private var currentVideoUri: Uri? = null
    private val _observablePlayVideoState = MutableLiveData<TokopediaPlayVideoState>()

    private val playerEventListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (!playWhenReady) _observablePlayVideoState.value = TokopediaPlayVideoState.Pause
            else when (playbackState) {
                Player.STATE_BUFFERING -> _observablePlayVideoState.value = TokopediaPlayVideoState.Buffering
                Player.STATE_READY -> _observablePlayVideoState.value = TokopediaPlayVideoState.Playing
                Player.STATE_IDLE -> _observablePlayVideoState.value = TokopediaPlayVideoState.NoMedia
                Player.STATE_ENDED -> _observablePlayVideoState.value = TokopediaPlayVideoState.Ended
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            //TODO("Maybe return error based on corresponding cause?")
            if (error != null && isBehindLiveWindow(error)) {
                val uri = currentVideoUri
                if (uri != null) playVideoWithUri(uri, videoPlayer.playWhenReady)
            } else _observablePlayVideoState.value = TokopediaPlayVideoState.Error(PlayVideoErrorException())
        }
    }

    val videoPlayer: ExoPlayer = ExoPlayerFactory.newSimpleInstance(applicationContext).apply {
        addListener(playerEventListener)
    }

    //region public method
    fun safePlayVideoWithUri(uri: Uri, autoPlay: Boolean = true) {
        if (uri != currentVideoUri) {
            currentVideoUri = uri
            playVideoWithUri(uri, autoPlay)
        }
        if (!videoPlayer.isPlaying) resumeCurrentVideo()
    }

    fun safePlayVideoWithUriString(uriString: String, autoPlay: Boolean) = safePlayVideoWithUri(Uri.parse(uriString), autoPlay)

    private fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true) {
        val mediaSource = getMediaSourceBySource(applicationContext, uri)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource)
    }

    //endregion

    //region player control
    fun resumeCurrentVideo() {
        if (videoPlayer.playbackState == ExoPlayer.STATE_ENDED) videoPlayer.seekTo(0)
        videoPlayer.playWhenReady = true
    }

    fun pauseCurrentVideo() {
        videoPlayer.playWhenReady = false
    }
    //endregion

    //region video state
    fun getObservablePlayVideoState(): LiveData<TokopediaPlayVideoState> = _observablePlayVideoState

    fun isVideoPlaying(): Boolean = videoPlayer.isPlaying

    fun getDurationVideo(): Long {
        return videoPlayer.duration
    }
    //endregion

    //region private method
    private fun getMediaSourceBySource(context: Context, uri: Uri?): MediaSource {
        val mDataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "home"))
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_SS -> SsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_DASH -> DashMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(mDataSourceFactory).createMediaSource(uri)
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    private fun getExoDataSourceFactoryByProtocol(protocol: PlayVideoProtocol): DataSource.Factory {
        return when (protocol) {
            is Http -> DefaultHttpDataSourceFactory(EXOPLAYER_AGENT)
            is Rtmp -> RtmpDataSourceFactory()
        }
    }
    //endregion

    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) return false

        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) return true
            cause = cause.cause
        }
        return false
    }

    fun releasePlayer() {
        videoPlayer.release()
    }
}