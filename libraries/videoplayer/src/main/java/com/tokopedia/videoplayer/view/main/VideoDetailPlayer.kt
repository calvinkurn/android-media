package com.tokopedia.videoplayer.view.main

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.tokopedia.videoplayer.R
import com.tokopedia.videoplayer.utils.VideoSourceProtocol
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
            val tag = VideoDetailPlayer::class.java.simpleName
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
        val videoSource = arguments?.getString(VIDEO_SOURCE, "")
        if (videoSource == null || videoSource.isEmpty()) {
            showToast(R.string.videoplayer_file_not_found)
            dismiss()
        } else {
            //testing purpose
            btnFile.setOnClickListener {
                initPlayer(videoSource)
            }

            btnHttp.setOnClickListener {
                //video source: URL
                val url = Uri.parse("https://r16---sn-axq7sn7z.googlevideo.com/videoplayback?expire=1560328405&ei=dGQAXYr1Mf2G7AS-6omwCw&ip=2a00%3A1838%3A35%3A2c%3A%3A9842&id=o-AO90l8KJgXsrbXKaBiB7n_zktcrQLe4NkZoGdO26TQgR&itag=22&source=youtube&requiressl=yes&mm=31%2C26&mn=sn-axq7sn7z%2Csn-5go7yn7l&ms=au%2Conr&mv=m&nh=IgpwcjAxLmxlZDAzKgkxMjcuMC4wLjE%2C&pl=56&initcwndbps=1065000&mime=video%2Fmp4&ratebypass=yes&dur=572.627&lmt=1540821371966085&mt=1560306669&fvip=2&c=WEB&txp=5431432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cratebypass%2Cdur%2Clmt&sig=ALgxI2wwRgIhALzCgrb917zjTN1PN_Ea1RB1_u4chaefp2fWkPV67vLbAiEA_LSwMXf6pjnbXln34tiNUWDpjg2wKDN7SqWe42FG-rY%3D&lsparams=mm%2Cmn%2Cms%2Cmv%2Cnh%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgXWODYee39sdb_XM52qLJvpfiFFm-rlmw-5mOQ-tHli4CIQCJ2c2Tc24ZJSni-PQ_oBlB-4D0YeJPYqUw2pindpa93A%3D%3D&video_id=PaKIZ7gJlRU&title=Linus+Torvalds+says+GPL+v3+violates+everything+that+GPLv2+stood+for")
                val protocol = VideoSourceProtocol.protocol("https://r16---sn-axq7sn7z.googlevideo.com/videoplayback?expire=1560328405&ei=dGQAXYr1Mf2G7AS-6omwCw&ip=2a00%3A1838%3A35%3A2c%3A%3A9842&id=o-AO90l8KJgXsrbXKaBiB7n_zktcrQLe4NkZoGdO26TQgR&itag=22&source=youtube&requiressl=yes&mm=31%2C26&mn=sn-axq7sn7z%2Csn-5go7yn7l&ms=au%2Conr&mv=m&nh=IgpwcjAxLmxlZDAzKgkxMjcuMC4wLjE%2C&pl=56&initcwndbps=1065000&mime=video%2Fmp4&ratebypass=yes&dur=572.627&lmt=1540821371966085&mt=1560306669&fvip=2&c=WEB&txp=5431432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cratebypass%2Cdur%2Clmt&sig=ALgxI2wwRgIhALzCgrb917zjTN1PN_Ea1RB1_u4chaefp2fWkPV67vLbAiEA_LSwMXf6pjnbXln34tiNUWDpjg2wKDN7SqWe42FG-rY%3D&lsparams=mm%2Cmn%2Cms%2Cmv%2Cnh%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgXWODYee39sdb_XM52qLJvpfiFFm-rlmw-5mOQ-tHli4CIQCJ2c2Tc24ZJSni-PQ_oBlB-4D0YeJPYqUw2pindpa93A%3D%3D&video_id=PaKIZ7gJlRU&title=Linus+Torvalds+says+GPL+v3+violates+everything+that+GPLv2+stood+for")
                Log.d("VideoDetailPlayer", videoSource)
                initPlayer(url, protocol)
            }

            btnRtmp.setOnClickListener {
                //video source: URL
                val url = Uri.parse("http://devimages.apple.com/samplecode/adDemo/ad.m3u8")
                val protocol = VideoSourceProtocol.protocol("http://devimages.apple.com/samplecode/adDemo/ad.m3u8")
                Log.d("VideoDetailPlayer", videoSource)
                initPlayer(url, protocol)
            }


//            //video source: file/uri
//            if (File(videoSource).exists()) {
//                initPlayer(videoSource)
//            } else {
//                //video source: URL
//                val url = Uri.parse(videoSource)
//                Log.d("VideoDetailPlayer", videoSource)
//                val protocol = VideoSourceProtocol.protocol(videoSource)
//                initPlayer(url, protocol)
//            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerOptions.release()
        } catch (ignored: Exception) {}
    }

    //file path
    private fun initPlayer(path: String) {
        val file = File(path)
        val mediaSource = buildMediaSource(
                source = Uri.fromFile(file),
                protocol = VideoSourceProtocol.File)
        initPlayer(mediaSource)
    }

    //uri type
    private fun initPlayer(uri: Uri, protocol: VideoSourceProtocol) {
        val mediaSource = buildMediaSource(
                source = uri,
                protocol = protocol)
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