package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.media.R
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.media.preview.ui.player.VideoControlView
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class VideoPreview(
    private val context: Context,
    private val videoPlayer: PickerVideoPlayer
) : BasePagerPreview, VideoControlView.Listener, PickerVideoPlayer.Listener {

    override val layout: Int
        get() = R.layout.view_item_preview_video

    private var videoControl: VideoControlView? = null
    private var isSkipUpdateState = false

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            val viewPlayer = it.findViewById<PlayerView>(R.id.video_preview)
            videoControl = it.findViewById(R.id.video_control)

            viewPlayer.player = videoPlayer.player()
            videoControl?.player = videoPlayer.player()

            videoPlayer.videoUrl = media.data.file?.path.orEmpty()

            videoPlayer.listener = this
            videoControl?.listener = this
        }
    }

    override fun isPlayingOnChanged(isPlaying: Boolean) {
        if (isSkipUpdateState) {
            return
        }
        videoControl?.updateCenterButtonState(isPlaying)
        videoControl?.showController(isPlaying)
    }

    override fun onCenterPauseButtonClicked() {
        videoPlayer.pause()
    }

    override fun onCenterPlayButtonClicked() {
        videoPlayer.resume()
    }

    override fun onScrubStart() {
        videoPlayer.pause()
        videoControl?.cleanHideJob()
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