package com.tokopedia.videoplayer.view.player

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.utils.*
import com.tokopedia.videoplayer.utils.RepeatMode
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.File

class TkpdVideoPlayer: Fragment() {

    companion object {
        //keys
        private const val VIDEO_SOURCE      = "video_uri"
        private const val VIDEO_CALLBACK    = "video_callback"

        //const
        private const val VIDEO_ROTATION_90 = 90f
        private const val EXOPLAYER_AGENT   = "exoplayer-codelab"
    }

    class Builder {
        private val videoPlayer = TkpdVideoPlayer()
        private val bundle = Bundle()

        fun transaction(containerId: Int, fragmentManager: FragmentManager) = apply {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, videoPlayer)
            transaction.commit()
            return this
        }

        fun videoSource(sourceMedia: String) = apply {
            bundle.putString(VIDEO_SOURCE, sourceMedia)
            return this
        }

        fun listener(callback: VideoPlayerListener) = apply {
            bundle.putSerializable(VIDEO_CALLBACK, callback)
            return this
        }

        fun build(): TkpdVideoPlayer {
            videoPlayer.arguments = bundle
            return videoPlayer
        }
    }

    private lateinit var playerOptions: SimpleExoPlayer
    private var callback: VideoPlayerListener ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendViewToBack(playerView) //utilities: send playerView in back of any views

        //catch source media from file, uri, or URL.
        val sourceMedia = arguments?.getString(VIDEO_SOURCE, "")

        //passing callback listener with serializable
        callback = arguments?.getSerializable(VIDEO_CALLBACK) as VideoPlayerListener?

        if (sourceMedia == null || sourceMedia.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            callback?.onPlayerError(PlayerException.SourceNotFound)
        } else {
            if (File(sourceMedia).exists()) {
                val file = Uri.fromFile(File(sourceMedia))
                initPlayer(file, VideoSourceProtocol.File)
            } else {
                val url = Uri.parse(sourceMedia)
                initPlayer(url, VideoSourceProtocol.protocol(context, sourceMedia))
            }
        }

        playerListener()
    }

    private fun playerListener() = playerOptions.addListener(object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            callback?.onPlayerStateChanged(playbackState)
            when (playbackState) {
                STATE_BUFFERING -> pgLoader.show()
                STATE_READY -> pgLoader.hide()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            callback?.onPlayerError(PlayerException.ExoPlayer)
        }
    })

    private fun initPlayer(source: Uri, protocol: VideoSourceProtocol) {
        try {
            playerOptions = ExoPlayerFactory.newSimpleInstance(context,
                    DefaultRenderersFactory(context),
                    DefaultTrackSelector(),
                    DefaultLoadControl())

            playerView.player = playerOptions

            //repeat mode
            playerOptions.repeatMode = if (callback?.repeatMode() != null) {
                callback?.repeatMode()!!
            } else {
                RepeatMode.REPEAT_MODE_OFF
            }

            //auto play enabled
            playerOptions.playWhenReady = true

            //fix bug: on kitkat devices
            if (SDK_INT == KITKAT) {
                playerView.rotation = VIDEO_ROTATION_90
            }

            playerOptions.prepare(
                    buildMediaSource(source, protocol),
                    /* reset position */
                    true,
                    /* reset state */
                    false)
        } catch (e: Exception) {
            showToast(R.string.videoplayer_invalid_player)
            callback?.onPlayerError(PlayerException.PlayerInitialize)
        }
    }

    private fun buildMediaSource(source: Uri, protocol: VideoSourceProtocol): MediaSource {
        return when (protocol) {
            //protocol supported: http, https
            VideoSourceProtocol.Http -> {
                ExtractorMediaSource.Factory(
                        DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
                        .createMediaSource(source)
            }
            //live streaming approach
            VideoSourceProtocol.Rtmp -> {
                ExtractorMediaSource.Factory(
                        RtmpDataSourceFactory())
                        .createMediaSource(source)
            }
            //file in local storage
            VideoSourceProtocol.File -> {
                val dataSpec = DataSpec(source)
                val fileDataSource = FileDataSource()
                fileDataSource.open(dataSpec)
                val dataFactory = DataSource.Factory { fileDataSource }
                ExtractorMediaSource.Factory(dataFactory)
                        .setExtractorsFactory(DefaultExtractorsFactory())
                        .createMediaSource(source)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is VideoPlayerListener) {
            callback = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerOptions.release()
        } catch (ignored: Exception) {}
    }

}