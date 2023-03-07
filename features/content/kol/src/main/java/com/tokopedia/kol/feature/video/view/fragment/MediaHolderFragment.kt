package com.tokopedia.kol.feature.video.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kol.R
import kotlinx.android.synthetic.main.media_player_view.*
import com.tokopedia.kol.common.player.VideoSourceProtocol
import com.tokopedia.kol.common.player.RepeatMode.REPEAT_MODE_OFF
import java.io.File
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.tokopedia.kotlin.extensions.view.*


class MediaHolderFragment : BaseDaggerFragment() {
    private var mediaType = TYPE_IMAGE
    private var mediaSource = ""
    private var onControllerTouch: OnControllerTouch? = null

    private var isReadyPlayed = true
    private var currentWindowIndex: Int = C.INDEX_UNSET
    private var currentPosition: Long = 0
    private var mExoPlayer: SimpleExoPlayer? = null
    private var isMute = false
    private var currentVol = 1f

    companion object {
        private const val ARG_MEDIA_SRC = "media_src"
        private const val ARG_MEDIA_TYPE = "media_type"

        private const val IS_READY_PLAYED_KEY = "is_ready_played"
        private const val CURRENT_WINDOW_INDEX_KEY = "current_window"
        private const val CURRENT_POSITION_KEY = "current_position"
        private const val CURRENT_MUTE_KEY = "current_mute"
        private const val CURRENT_VOLUME_KEY = "current_volume"

        const val TYPE_IMAGE = "image"
        const val TYPE_VIDEO = "video"

        private const val VIDEO_ROTATION_90 = 90f
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"

        fun createInstance(mediaSource: String, type: String) = MediaHolderFragment().also {
            it.arguments = Bundle().apply {
                putString(ARG_MEDIA_SRC, mediaSource)
                putString(ARG_MEDIA_TYPE, type)
            }
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnControllerTouch){
            onControllerTouch = context
        }
        arguments?.let {
            mediaType = it.getString(ARG_MEDIA_TYPE, TYPE_IMAGE)
            mediaSource = it.getString(ARG_MEDIA_SRC, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.media_player_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mediaSource.isBlank()) return

        image_preview.shouldShowWithAction(mediaType == TYPE_IMAGE){
            image_preview.loadImageWithoutPlaceholder(mediaSource)
        }
        video_player.showWithCondition(mediaType == TYPE_VIDEO)

        savedInstanceState?.let {
            isReadyPlayed = savedInstanceState.getBoolean(IS_READY_PLAYED_KEY, false)
            currentWindowIndex = savedInstanceState.getInt(CURRENT_WINDOW_INDEX_KEY, 0)
            currentPosition = savedInstanceState.getLong(CURRENT_POSITION_KEY, 0)
            isMute = savedInstanceState.getBoolean(CURRENT_MUTE_KEY, false)
            currentVol = savedInstanceState.getFloat(CURRENT_VOLUME_KEY, 1f)
        }

        video_player.setControllerVisibilityListener {
            onControllerTouch?.onTouch(it == View.VISIBLE)
        }
        val volumeControl: ImageView = video_player.findViewById(R.id.volume_control)
        volumeControl.setOnClickListener {
            if (!isMute){
                currentVol = mExoPlayer?.volume ?: 1f
            }
            isMute = !isMute
            context?.let { volumeControl.setImageDrawable(
                    ContextCompat.getDrawable(it,if (isMute) R.drawable.ic_af_volume_off else R.drawable.ic_af_volume_on))
            }
            mExoPlayer?.volume = if (isMute) 0f else currentVol
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23 && mediaType == TYPE_VIDEO && userVisibleHint){
            playVideo(mediaSource)
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23 || mExoPlayer == null) && mediaType == TYPE_VIDEO && userVisibleHint){
            playVideo(mediaSource)
        }
    }

    override fun onPause() {
        backupState()
        if (Util.SDK_INT <= 23 && mediaType == TYPE_VIDEO){
            releaseExoPlayer()
        }
        super.onPause()
    }

    override fun onStop() {
        backupState()
        if (Util.SDK_INT > 23 && mediaType == TYPE_VIDEO){
            releaseExoPlayer()
        }
        super.onStop()
    }
    
    private fun playVideo(source: String) {
        try {
            if (isFromLocalFile(mediaSource)){
                val file = Uri.fromFile(File(source))
                initPlayer(file, VideoSourceProtocol.File)
            } else {
                val url = Uri.parse(source)
                initPlayer(url, VideoSourceProtocol.protocol(source))
            }
        } catch (t: Throwable) {
            showErrorLayout(t !is UnrecognizedInputFormatException)
        }

        //player listener
        initPlayerListener()
    }
    
    private fun initPlayerListener() {
        mExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    Player.STATE_READY -> {
                        loading?.gone()
                        error_layout?.gone()
                        video_player?.visible()
                    }
                    Player.STATE_BUFFERING, Player.STATE_IDLE -> {
                        loading?.visible()
                        error_layout?.gone()
                    }
                    else -> { }
                }
            }
        })
    }

    private fun showErrorLayout(isShowRetry: Boolean = true) {
        error_layout?.visible()
        btn_retry.shouldShowWithAction(isShowRetry){
            btn_retry.setOnClickListener {
                backupState()
                releaseExoPlayer()
                playVideo(mediaSource)
            }
        }
    }

    private fun initPlayer(url: Uri?, protocol: VideoSourceProtocol) {
        if (url == null) return

        try {
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val loadControl = DefaultLoadControl()

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext(),
                    DefaultRenderersFactory(requireContext()), trackSelector, loadControl)

            video_player.player = mExoPlayer
            mExoPlayer?.repeatMode = REPEAT_MODE_OFF
            mExoPlayer?.playWhenReady = isReadyPlayed
            val isHasStartPosition = currentWindowIndex != C.INDEX_UNSET
            if (isHasStartPosition) {
                mExoPlayer?.seekTo(currentWindowIndex, currentPosition)
            }

            video_player.controllerShowTimeoutMs = 0
            if (isMute){
                mExoPlayer?.volume = 0f
            }

            mExoPlayer?.prepare(buildMediaSource(url, protocol), !isHasStartPosition, false)
        } catch (t: Throwable){
            showErrorLayout(t !is UnrecognizedInputFormatException)
        }
    }

    private fun buildMediaSource(source: Uri, protocol: VideoSourceProtocol): MediaSource {
        return when (protocol) {
            //protocol supported: http, https
            is VideoSourceProtocol.Http -> {
                ExtractorMediaSource.Factory(
                        DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
                        .createMediaSource(source)
            }
            //live streaming approach
            is VideoSourceProtocol.Rtmp -> {
                ExtractorMediaSource.Factory(
                        RtmpDataSourceFactory())
                        .createMediaSource(source)
            }
            //file in local storage
            is VideoSourceProtocol.File -> {
                val dataSpec = DataSpec(source)
                val fileDataSource = FileDataSource()
                fileDataSource.open(dataSpec)
                val dataFactory = DataSource.Factory { fileDataSource }
                ExtractorMediaSource.Factory(dataFactory)
                        .setExtractorsFactory(DefaultExtractorsFactory())
                        .createMediaSource(source)
            }
            is VideoSourceProtocol.InvalidFormat -> {
                throw Exception(getString(protocol.message))
            }
        }
    }

    private fun isFromLocalFile(mediaSource: String): Boolean = File(mediaSource).exists()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_WINDOW_INDEX_KEY, currentWindowIndex)
        outState.putLong(CURRENT_POSITION_KEY, currentPosition)
        outState.putBoolean(IS_READY_PLAYED_KEY, isReadyPlayed)
        outState.putBoolean(CURRENT_MUTE_KEY, isMute)
        outState.putFloat(CURRENT_VOLUME_KEY, currentVol)
    }

    fun fragmentShowing(){
        if(mediaType  == TYPE_VIDEO)
            playVideo(mediaSource)
    }

    fun fragmentHiding(){
        if (mediaType == TYPE_VIDEO) {
            backupState()
            releaseExoPlayer()
        }
    }

    override fun onDestroyView() {
        releaseExoPlayer()
        super.onDestroyView()
    }

    override fun onDestroy() {
        releaseExoPlayer()
        super.onDestroy()
    }

    private fun releaseExoPlayer() {
        mExoPlayer?.stop()
        mExoPlayer?.release()
        mExoPlayer = null
    }

    private fun backupState() {
        mExoPlayer?.let {
            isReadyPlayed = it.playWhenReady
            currentPosition = it.currentPosition
            currentWindowIndex = it.currentWindowIndex
        }
    }

    interface OnControllerTouch {
        fun onTouch(isVisible: Boolean)
    }
}
