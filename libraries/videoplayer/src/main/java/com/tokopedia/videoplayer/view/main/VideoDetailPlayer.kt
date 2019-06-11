package com.tokopedia.videoplayer.view.main

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.utils.showToast
import kotlinx.android.synthetic.main.fragment_video_preview.*
import java.io.File

/**
 * Created by isfaaghyth on 25/04/19.
 * github: @isfaaghyth
 */
class VideoDetailPlayer: BottomSheetDialogFragment() {

    companion object {
        //keys
        private const val VIDEO_SOURCE      = "video_uri"

        //const variables
        private const val EXOPLAYER_AGENT   = "exoplayer-codelab"
        private const val VIDEO_ROTATION_90 = 90f

        fun set(videoSource: String): BottomSheetDialogFragment {
            val videoPlayer = VideoDetailPlayer()
            val bundle = Bundle()
            bundle.putString(VIDEO_SOURCE, videoSource)
            videoPlayer.arguments = bundle
            return videoPlayer
        }

        fun show(videoSource: String, fragmentManager: FragmentManager) {
            val tag = "VideoDetailPlayer"
            set(videoSource).show(fragmentManager, tag)
        }
    }

    private lateinit var playerOptions: SimpleExoPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss()
                    }
                }
            })
        }

        initView()
    }

    fun initView() {
        btnClose.setOnClickListener { dismiss() }

        //showing close button on top
        sendViewToBack(playerView)

        //get video source path
        val videoSource = "rtmp://video.tokopedia.com/toplive/toplive?auth_key=1560407795-0-0-5706f231ac5a1c0718f17eac9669950b" as String?
        if (videoSource == null || videoSource.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            dismiss()
        } else {
            //video source: file/uri
            if (File(videoSource).exists()) {
                initPlayer(videoSource)
            } else {
                //video source: URL
                val url = Uri.parse(videoSource)
                initPlayer(url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerOptions.release()
        } catch (ignored: Exception) {}
    }

    private fun initPlayer(path: String) {
        val file = File(path)
        val mediaSource = buildMediaSource(Uri.fromFile(file), true)
        initPlayer(mediaSource)
    }

    private fun initPlayer(uri: Uri) {
        val mediaSource = buildMediaSource(uri, false)
        initPlayer(mediaSource)
    }

    private fun initPlayer(mediaSource: MediaSource) {
        try {
            playerOptions = ExoPlayerFactory.newSimpleInstance(context,
                    DefaultRenderersFactory(context),
                    DefaultTrackSelector(),
                    DefaultLoadControl())
            playerView.player = playerOptions

            //auto play enabled
            playerOptions.playWhenReady = true

            //fix bug: rotate on kitkat devices
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT) {
                playerView.rotation = VIDEO_ROTATION_90
            }

            playerOptions.prepare(mediaSource, true, false)
        } catch (e: Exception) {
            showToast(R.string.videoplayer_invalid_player)
            dismiss()
        }
    }

    private fun buildMediaSource(uri: Uri, isFile: Boolean): MediaSource {
        return if (isFile) {
            val dataSpec = DataSpec(uri)
            val fileDataSource = FileDataSource()
            fileDataSource.open(dataSpec)
            val dataFactory = DataSource.Factory { fileDataSource }
            ExtractorMediaSource.Factory(dataFactory)
                    .setExtractorsFactory(DefaultExtractorsFactory())
                    .createMediaSource(uri)
        } else {
            //rtmp
            ExtractorMediaSource.Factory(
                    RtmpDataSourceFactory())
                    .createMediaSource(uri)

            //http
//            ExtractorMediaSource.Factory(
//                    DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
//                    .createMediaSource(uri)
        }
    }

}