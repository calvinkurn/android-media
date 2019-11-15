package com.tokopedia.product.detail.view.fragment

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.videoplayer.state.RepeatMode
import com.tokopedia.videoplayer.state.VideoSourceProtocol
import kotlinx.android.synthetic.main.video_picture_view.*
import java.io.File


class VideoPictureFragment : BaseDaggerFragment() {

    private var mediaType = TYPE_IMAGE
    private var mediaSource = ""
    private var mediaPosition = 0
    var onPictureClickListener: ((Int) -> Unit)? = null

    private var isReadyPlayed = true
    private var currentWindowIndex: Int = C.INDEX_UNSET
    private var currentPosition: Long = 0
    private var isMute = false
    private var currentVol = 1f
    private var mExoPlayer: SimpleExoPlayer? = null

    companion object {

        private const val IS_READY_PLAYED_KEY = "is_ready_played"
        private const val CURRENT_WINDOW_INDEX_KEY = "current_window"
        private const val CURRENT_POSITION_KEY = "current_position"
        private const val CURRENT_MUTE_KEY = "current_mute"
        private const val CURRENT_VOLUME_KEY = "current_volume"

        private const val TYPE_IMAGE = "image"
        private const val TYPE_VIDEO = "video"

        private const val VIDEO_ROTATION_90 = 90f
        private const val EXOPLAYER_AGENT = "com.tkpd.exoplayer"

        private const val ARG_MEDIA_SRC = "media_src"
        private const val ARG_MEDIA_TYPE = "media_type"
        private const val ARG_MEDIA_POSITION = "media_position"

        fun createInstance(mediaSource: String, type: String, position: Int) = VideoPictureFragment().also {
            it.arguments = Bundle().apply {
                putString(ARG_MEDIA_SRC, mediaSource)
                putString(ARG_MEDIA_TYPE, type)
                putInt(ARG_MEDIA_POSITION, position)
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.let {
            mediaType = it.getString(ARG_MEDIA_TYPE, TYPE_IMAGE)
            mediaSource = it.getString(ARG_MEDIA_SRC, "")
                mediaPosition = it.getInt(ARG_MEDIA_POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.video_picture_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            onPictureClickListener?.invoke(mediaPosition)
        }

        if (mediaSource.isBlank()) return

        if (mediaType == TYPE_IMAGE) {
            volume_pdp.hide()
            img_pdp_video.show()
            video_player_pdp.hide()
            loading_pdp.hide()
            val currentOrientation = context?.resources?.configuration?.orientation
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                img_pdp_video.scaleType = ImageView.ScaleType.FIT_CENTER
                if (mediaSource.isNotEmpty()) {
                    Glide.with(view.context)
                            .load(mediaSource)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .fitCenter()
                            .into(img_pdp_video)
                }
            } else {
                img_pdp_video.scaleType = ImageView.ScaleType.CENTER_CROP
                if (mediaSource.isNotEmpty()) {
                    Glide.with(view.context)
                            .load(mediaSource)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop()
                            .into(img_pdp_video)
                }
            }
        } else {
            volume_pdp.show()
            video_player_pdp.show()
            loading_pdp.show()
            img_pdp_video.hide()
        }

        savedInstanceState?.let {
            isReadyPlayed = savedInstanceState.getBoolean(IS_READY_PLAYED_KEY, false)
            currentWindowIndex = savedInstanceState.getInt(CURRENT_WINDOW_INDEX_KEY, 0)
            currentPosition = savedInstanceState.getLong(CURRENT_POSITION_KEY, 0)
            isMute = savedInstanceState.getBoolean(CURRENT_MUTE_KEY, false)
            currentVol = savedInstanceState.getFloat(CURRENT_VOLUME_KEY, 1f)
        }

        context?.let {
            volume_pdp.setImageDrawable(
                    ContextCompat.getDrawable(it, if (isMute) R.drawable.ic_volume_off_black else R.drawable.ic_volume_up_black))
        }

        volume_pdp.setOnClickListener {
            if (isMute) {
                //turn on volume
                isMute = false
                mExoPlayer?.volume = 1f
            } else {
                //turn off volume
                isMute = true
                mExoPlayer?.volume = 0f
            }
            context?.let {
                volume_pdp.setImageDrawable(
                        ContextCompat.getDrawable(it, if (isMute) R.drawable.ic_volume_off_black else R.drawable.ic_volume_up_black))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23 && mediaType == TYPE_VIDEO && userVisibleHint) {
            playVideo(mediaSource)
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23 || mExoPlayer == null) && mediaType == TYPE_VIDEO && userVisibleHint) {
            playVideo(mediaSource)
        }
    }

    override fun onPause() {
        backupState()
        if (Util.SDK_INT <= 23 && mediaType == TYPE_VIDEO) {
            releaseExoPlayer()
        }
        super.onPause()
    }

    fun pauseVideo() {
        mExoPlayer?.playWhenReady = false
    }

    override fun onStop() {
        backupState()
        if (Util.SDK_INT > 23 && mediaType == TYPE_VIDEO) {
            releaseExoPlayer()
        }
        super.onStop()
    }


    private fun playVideo(source: String) {
        if (isFromLocalFile(mediaSource)) {
            val file = Uri.fromFile(File(source))
            initPlayer(file, VideoSourceProtocol.File)
        } else {
            val url = Uri.parse(source)
            initPlayer(url, VideoSourceProtocol.protocol(source))
        }
        initPlayerListener()
    }

    private fun initPlayerListener() {
        mExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        loading_pdp.gone()
                    }
                    else -> {
                        loading_pdp.visible()
                    }
                }
            }
        })
    }

    private fun isConnectedToWifi(): Boolean {
        val wifi: WifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifi.isWifiEnabled) {
            val wifiInfo = wifi.connectionInfo

            if (wifiInfo.networkId == -1) {
                return false
            }

            return true
        } else {
            return false
        }
    }

    private fun initPlayer(url: Uri?, protocol: VideoSourceProtocol) {
        if (url == null) return

        try {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
            video_player_pdp.useController = true
            video_player_pdp.player = mExoPlayer
            mExoPlayer?.repeatMode = RepeatMode.REPEAT_MODE_OFF

            if (isConnectedToWifi()) {
                mExoPlayer?.playWhenReady = isReadyPlayed
            } else {
                mExoPlayer?.playWhenReady = false
            }

            val isHasStartPosition = currentWindowIndex != C.INDEX_UNSET
            if (isHasStartPosition) {
                mExoPlayer?.seekTo(currentWindowIndex, currentPosition)
            }

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                video_player_pdp.rotation = VIDEO_ROTATION_90
            }

            video_player_pdp.controllerShowTimeoutMs = 1000
            if (isMute) {
                mExoPlayer?.volume = 0f
            }

            mExoPlayer?.prepare(buildMediaSource(url, protocol), !isHasStartPosition, false)
        } catch (t: Throwable) {

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
            else -> {
                ExtractorMediaSource.Factory(
                        DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
                        .createMediaSource(source)
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

    fun imVisible() {
        // used for OkBuck purpose
        if (mediaType == TYPE_VIDEO) {
            playVideo(mediaSource)
        }
    }

    fun imInvisible() {
        backupState()
        releaseExoPlayer()
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
}