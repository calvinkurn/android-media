package com.tokopedia.play_common.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tokopedia.play_common.type.Http
import com.tokopedia.play_common.type.Rtmp
import com.tokopedia.play_common.type.PlayVideoProtocol
import com.tokopedia.play_common.type.getVideoTypeByUri

/**
 * Created by jegul on 29/11/19
 */
class TokopediaPlayService : Service() {

    companion object {
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"

        fun start(context: Context) {
            context.startService(
                    Intent(context, TokopediaPlayService::class.java)
            )
        }

        fun stop(context: Context) {
            context.stopService(
                    Intent(context, TokopediaPlayService::class.java)
            )
        }
    }

    private val mBinder = LocalBinder()

    lateinit var videoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        videoPlayer = ExoPlayerFactory.newSimpleInstance(this).apply {
            playWhenReady = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.release()
    }

    //region public method
    fun playVideoWithUri(uri: Uri) {
        val videoType = PlayVideoProtocol.getVideoTypeByUri(uri)
        val mediaSource = getMediaSourceByVideoType(videoType)
        videoPlayer.prepare(mediaSource, true, true)
    }

    fun playVideoWithString(uriString: String) = playVideoWithUri(Uri.parse(uriString))
    //endregion

    //region private method
    private fun getMediaSourceByVideoType(protocol: PlayVideoProtocol): MediaSource {
        return ProgressiveMediaSource.Factory(
                getExoDataSourceFactoryByVideoType(protocol)
        ).createMediaSource(protocol.uri)
    }

    private fun getExoDataSourceFactoryByVideoType(protocol: PlayVideoProtocol): DataSource.Factory {
        return when (protocol) {
            is Http -> DefaultHttpDataSourceFactory(EXOPLAYER_AGENT)
            is Rtmp -> RtmpDataSourceFactory()
        }
    }
    //endregion

    inner class LocalBinder : Binder() {

        val service: TokopediaPlayService
            get() = this@TokopediaPlayService
    }
}