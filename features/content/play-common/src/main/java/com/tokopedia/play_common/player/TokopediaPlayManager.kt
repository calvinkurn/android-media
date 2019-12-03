package com.tokopedia.play_common.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tokopedia.play_common.type.Http
import com.tokopedia.play_common.type.Rtmp
import com.tokopedia.play_common.type.TokopediaPlayVideoType
import com.tokopedia.play_common.type.getVideoTypeByUri

/**
 * Created by jegul on 03/12/19
 */
class TokopediaPlayManager private constructor(appCtx: Context) {

    companion object {
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"
        private var INSTANCE: TokopediaPlayManager? = null

        fun getInstance(appCtx: Context): TokopediaPlayManager {
            if (INSTANCE == null) INSTANCE = TokopediaPlayManager(appCtx)
            return INSTANCE!!
        }
    }

    val videoPlayer: ExoPlayer = ExoPlayerFactory.newSimpleInstance(appCtx).apply {
        playWhenReady = true
    }

    //region public method
    fun playVideoWithUri(uri: Uri) {
        val videoType = TokopediaPlayVideoType.getVideoTypeByUri(uri)
        val mediaSource = getMediaSourceByVideoType(videoType)
        videoPlayer.prepare(mediaSource, true, true)
    }

    fun playVideoWithString(uriString: String) = playVideoWithUri(Uri.parse(uriString))
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