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
import com.google.android.exoplayer2.Player
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
import com.tokopedia.videoplayer.state.*
import com.tokopedia.videoplayer.state.Player.Companion.STATE_BUFFERING
import com.tokopedia.videoplayer.state.Player.Companion.STATE_READY
import com.tokopedia.videoplayer.state.RepeatMode
import com.tokopedia.videoplayer.utils.*
import kotlinx.android.synthetic.main.fragment_video_player.*
import java.io.File

class TkpdVideoPlayer: Fragment(), ControllerListener {

    companion object {
        //keys
        private const val VIEW_MODEL = "video_model"
        private const val VIDEO_SOURCE = "video_uri"
        private const val VIDEO_CALLBACK = "video_callback"
        private const val REPEAT_MODE = "repeat_mode"
        private const val NATIVE_CONTROLLER = "native_controller"
        private const val PLAYER_TYPE = "player_type"

        //const
        private const val VIDEO_ROTATION_90 = 90f
        private const val EXOPLAYER_AGENT = "exoplayer-codelab"
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

        fun repeatMode(mode: Int) = apply {
            bundle.putInt(REPEAT_MODE, mode)
        }

        fun videoSource(sourceMedia: String) = apply {
            bundle.putString(VIDEO_SOURCE, sourceMedia)
            return this
        }

        fun listener(callback: VideoPlayerListener) = apply {
            bundle.putParcelable(VIDEO_CALLBACK, callback)
            return this
        }

        fun type(type: Int) = apply {
            bundle.putInt(PLAYER_TYPE, type)
            return this
        }

        fun controller(isActive: Boolean) = apply {
            bundle.putBoolean(NATIVE_CONTROLLER, isActive)
            return this
        }

        fun build(): TkpdVideoPlayer {
            videoPlayer.arguments = bundle
            return videoPlayer
        }
    }

    private var playerOptions: SimpleExoPlayer?= null
    private var callback: VideoPlayerListener?= null

    private var viewModel = TkpdPlayerViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendViewToBack(playerView) //utilities: send playerView in back of any views

        //catch video detail properties
        when {
            savedInstanceState != null -> {
                viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: TkpdPlayerViewModel()
            }
            arguments != null -> {
                viewModel.videoSource = arguments!!.getString(VIDEO_SOURCE, "")
                viewModel.repeatMode = arguments!!.getInt(REPEAT_MODE, RepeatMode.REPEAT_MODE_OFF)
                viewModel.nativeController = arguments!!.getBoolean(NATIVE_CONTROLLER, PlayerController.ON)
                viewModel.playerType = arguments!!.getInt(PLAYER_TYPE, PlayerType.DEFAULT)

                //passing callback listener with serializable
                callback = arguments?.getParcelable(VIDEO_CALLBACK) as VideoPlayerListener?

                //native controller visibility
                playerView?.useController = viewModel.nativeController
                pgLoader?.showWithCondition(viewModel.nativeController)

            }
            else -> activity?.finish()
        }

        if (viewModel.videoSource.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            callback?.onPlayerError(PlayerException.SourceNotFound)
        }
    }

    private fun playVideo(source: String) {
        if (File(source).exists()) {
            val file = Uri.fromFile(File(source))
            initPlayer(file, VideoSourceProtocol.File)
        } else {
            val url = Uri.parse(source)
            initPlayer(url, VideoSourceProtocol.protocol(context, source))
        }

        //player listener
        playerListener()
    }

    private fun playerListener() = playerOptions?.addListener(object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            callback?.onPlayerStateChanged(playbackState)
            when (playbackState) {
                STATE_BUFFERING -> {
                    pgLoader?.showWithCondition(viewModel.nativeController)
                    dimBackground?.show()
                }
                STATE_READY -> {
                    dimBackground?.hide()
                }
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
            playerOptions?.repeatMode = viewModel.repeatMode

            //auto play enabled
            playerOptions?.playWhenReady = true

            //fix bug: on kitkat devices
            if (SDK_INT == KITKAT) {
                playerView.rotation = VIDEO_ROTATION_90
            }

            playerOptions?.prepare(
                    buildMediaSource(source, protocol),
                    /* reset position */
                    viewModel.stateVideoPosition <= 0,
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

    override fun resume() {
        playerOptions?.playWhenReady = true
        playerOptions?.playbackState
    }

    override fun pause() {
        playerOptions?.playWhenReady = false
        playerOptions?.playbackState
    }

    override fun stop() {
        playerOptions?.stop()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is VideoPlayerListener) {
            callback = context
        }
    }

    override fun onStart() {
        super.onStart()
        playVideo(viewModel.videoSource)
    }

    override fun onResume() {
        super.onResume()
        //get current position and seeking of video player
        if (viewModel.playerType == PlayerType.DEFAULT) {
            playerOptions?.seekTo(viewModel.stateVideoPosition)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerOptions?.release()
    }

    override fun onStop() {
        super.onStop()
        playerOptions?.stop()

        //save current position on video player
        viewModel.stateVideoPosition = playerOptions?.currentPosition!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerOptions?.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(VIEW_MODEL, viewModel)
    }

}