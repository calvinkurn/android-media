package com.tokopedia.videoplayer.view.main

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tokopedia.videoplayer.R
import kotlinx.android.synthetic.main.fragment_video_preview.*

/**
 * Created by isfaaghyth on 25/04/19.
 * github: @isfaaghyth
 */
class VideoDetailPlayer: BottomSheetDialogFragment() {

    companion object {
        //keys
        private const val VIDEO_SOURCE = "video_uri"

        //const variables
        private const val EXOPLAYER_AGENT = "exoplayer-codelab"

        fun show(videoSource: String): BottomSheetDialogFragment {
            val videoPlayer = VideoDetailPlayer()
            val bundle = Bundle()
            bundle.putString(VIDEO_SOURCE, videoSource)
            videoPlayer.arguments = bundle
            return videoPlayer
        }
    }

    private lateinit var playerOptions: SimpleExoPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get video source path
        val videoSource = arguments?.getString(VIDEO_SOURCE)
        if (videoSource == null) {
            dismiss()
        } else {
            videoPlayerInit(videoSource)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            playerOptions.release()
        } catch (ignored: Exception) {}
    }

    private fun videoPlayerInit(source: String) {
        playerOptions = ExoPlayerFactory.newSimpleInstance(context,
                DefaultRenderersFactory(context),
                DefaultTrackSelector(),
                DefaultLoadControl())
        videoPlayerView.player = playerOptions

        val mediaSource = buildMediaSource(Uri.parse(source))
        playerOptions.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory(EXOPLAYER_AGENT))
                .createMediaSource(uri)
    }

}