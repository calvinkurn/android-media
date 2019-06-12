package com.tokopedia.videoplayer.view.widget

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.STATE_BUFFERING
import com.google.android.exoplayer2.Player.STATE_READY
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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.utils.VideoSourceProtocol
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.utils.showToast
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.File

class TkpdVideoPlayer: Fragment() {

    companion object {
        //keys
        private const val VIDEO_SOURCE      = "video_uri"

        //const
        private const val VIDEO_ROTATION_90 = 90f
        private const val EXOPLAYER_AGENT   = "exoplayer-codelab"

        fun set(sourceMedia: String, containerId: Int, fragmentManager: FragmentManager): Fragment {
            val videoPlayer = TkpdVideoPlayer()

            //commit
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, videoPlayer)
            transaction.commit()

            //send data
            val bundle = Bundle()
            bundle.putString(VIDEO_SOURCE, sourceMedia)
            videoPlayer.arguments = bundle
            return videoPlayer
        }
    }

    private lateinit var playerOptions: SimpleExoPlayer
    private lateinit var listener: VideoPlayerListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //utilities
        sendViewToBack(playerView)

        val sourceMedia = arguments?.getString(VIDEO_SOURCE, "")
        if (sourceMedia == null || sourceMedia.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            listener.onPlayerError()
        } else {
            if (File(sourceMedia).exists()) {
                val file = Uri.fromFile(File(sourceMedia))
                loadPlayer(file, VideoSourceProtocol.File)
            } else {
                val url = Uri.parse(sourceMedia)
                loadPlayer(url, VideoSourceProtocol.protocol(sourceMedia))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerOptions.release()
        } catch (ignored: Exception) {}
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is VideoPlayerListener) {
            listener = context
        }
    }

    private fun loadPlayer(uri: Uri, protocol: VideoSourceProtocol) {
        initPlayer(uri, protocol)
    }

    private fun initPlayer(source: Uri, protocol: VideoSourceProtocol) {
        try {
            playerOptions = ExoPlayerFactory.newSimpleInstance(context,
                    DefaultRenderersFactory(context),
                    DefaultTrackSelector(),
                    DefaultLoadControl())

            playerView.player = playerOptions

            //auto play enabled
            playerOptions.playWhenReady = listener.autoPlay()

            //fix bug: on kitkat devices
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT) {
                playerView.rotation = VIDEO_ROTATION_90
            }

            playerOptions.prepare(
                    buildMediaSource(source, protocol),
                    true,
                    false)

            playerOptions.addListener(object : Player.EventListener {
                override fun onPlayerError(error: ExoPlaybackException?) {
                    listener.onPlayerError()
                }

                override fun onLoadingChanged(isLoading: Boolean) {
                    pgLoader.showWithCondition(isLoading)
                    listener.onLoadingChanged(isLoading)
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    listener.onStateChanged(playbackState)
                    when (playbackState) {
                        STATE_BUFFERING -> {
                            pgLoader.show()
                        }
                        STATE_READY -> {
                            pgLoader.hide()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            showToast(R.string.videoplayer_invalid_player)
            listener.onPlayerError()
        }
    }

    private fun buildMediaSource(source: Uri, protocol: VideoSourceProtocol): MediaSource {
        return when (protocol) {
            VideoSourceProtocol.Http -> {
                ExtractorMediaSource.Factory(
                        DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
                        .createMediaSource(source)
            }
            VideoSourceProtocol.Rtmp -> {
                ExtractorMediaSource.Factory(
                        RtmpDataSourceFactory())
                        .createMediaSource(source)
            }
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

}