package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.os.Handler
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.media.R
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel
import android.os.Looper
import android.util.Log

class VideoPreview(
    private val context: Context,
    val videoPlayer: PickerVideoPlayer
) : BasePagerPreview, VideoControlView.Listener, PickerVideoPlayer.Listener {

    override val layout: Int
        get() = R.layout.view_item_preview_video

    private lateinit var videoControl: VideoControlView
    private var isSkipUpdateState = false

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            val viewPlayer = it.findViewById<PlayerView>(R.id.video_preview)
            videoControl = it.findViewById(R.id.video_control)

            viewPlayer.player = videoPlayer.player()
            videoControl.player = videoPlayer.player()

            videoPlayer.videoUrl = media.data.path

            videoPlayer.listener = this
            videoControl.listener = this
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if(isSkipUpdateState){
            return
        }
        videoControl.updateCenterButtonState(isPlaying)
    }

    override fun onVideoLoop() {
        videoControl.showController()
    }

    override fun onCenterPauseButtonClicked() {
        videoPlayer.pause()
    }

    override fun onCenterPlayButtonClicked() {
        videoPlayer.resume()
    }

    override fun onScrubStart() {
        videoPlayer.pause()
        videoControl.cleanHideJob()
        isSkipUpdateState = true
    }

    override fun onScrubStop() {
        videoPlayer.resume()
        isSkipUpdateState = false
    }

    override fun onScrubMove(position: Long) {
        isSkipUpdateState = false
        videoPlayer.player().seekTo(position)
    }
}