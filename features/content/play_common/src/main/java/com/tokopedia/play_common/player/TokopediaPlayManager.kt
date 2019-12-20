package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tokopedia.play_common.exception.PlayVideoErrorException
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.play_common.type.Http
import com.tokopedia.play_common.type.Rtmp
import com.tokopedia.play_common.type.TokopediaPlayVideoType
import com.tokopedia.play_common.type.getVideoTypeByUri

/**
 * Created by jegul on 03/12/19
 */
class TokopediaPlayManager private constructor(applicationContext: Context) {

    companion object {
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"

        @Volatile
        private var INSTANCE: TokopediaPlayManager? = null

        @JvmStatic
        fun getInstance(applicationContext: Context): TokopediaPlayManager {
            return INSTANCE ?: synchronized(this) {
                TokopediaPlayManager(applicationContext).also {
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
            _observablePlayVideoState.value = TokopediaPlayVideoState.Error(PlayVideoErrorException())
        }
    }

    val videoPlayer: ExoPlayer = ExoPlayerFactory.newSimpleInstance(applicationContext).apply {
        addListener(playerEventListener)
    }

    //region public method
    fun safePlayVideoWithUri(uri: Uri, autoPlay: Boolean = true) {
        if (!videoPlayer.isPlaying) playVideoWithUri(uri, autoPlay)
    }

    fun safePlayVideoWithUriString(uriString: String, autoPlay: Boolean) = safePlayVideoWithUri(Uri.parse(uriString), autoPlay)

    fun playVideoWithUri(uri: Uri, autoPlay: Boolean = true) {
        val videoType = TokopediaPlayVideoType.getVideoTypeByUri(uri)
        val mediaSource = getMediaSourceByVideoType(videoType)
        videoPlayer.playWhenReady = autoPlay
        videoPlayer.prepare(mediaSource)
    }

    fun playVideoWithString(uriString: String, autoPlay: Boolean = true) = playVideoWithUri(Uri.parse(uriString), autoPlay)
    //endregion

    //region player control
    fun resumeCurrentVideo() {
        videoPlayer.playWhenReady = true
    }

    fun pauseCurrentVideo() {
        videoPlayer.playWhenReady = false
    }
    //endregion

    //region video state
    fun getObservablePlayVideoState(): LiveData<TokopediaPlayVideoState> = _observablePlayVideoState

    fun isVideoPlaying(): Boolean = videoPlayer.isPlaying
    //endregion

    //region private method
    private fun getMediaSourceByVideoType(type: TokopediaPlayVideoType): MediaSource {
        return ProgressiveMediaSource.Factory(
                getExoDataSourceFactoryByVideoType(type)
        ).createMediaSource(type.uri)
    }

    private fun getExoDataSourceFactoryByVideoType(type: TokopediaPlayVideoType): DataSource.Factory {
        return when (type) {
            is Http -> DefaultHttpDataSourceFactory(EXOPLAYER_AGENT)
            is Rtmp -> RtmpDataSourceFactory()
        }
    }
    //endregion

    fun releasePlayer() {
        videoPlayer.release()
    }
}