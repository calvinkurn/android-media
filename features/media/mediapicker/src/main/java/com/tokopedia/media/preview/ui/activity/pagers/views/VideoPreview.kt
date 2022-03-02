package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel

class VideoPreview(
    private val context: Context,
    val videoPlayer: PickerVideoPlayer
) : BasePagerPreview {

    override val layout: Int
        get() = R.layout.view_item_preview_video

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            val viewPlayer = it.findViewById<PlayerView>(R.id.video_preview)
            val videoControl = it.findViewById<PlayerControlView>(R.id.video_control)

            viewPlayer.player = videoPlayer.player()
            videoControl.player = videoPlayer.player()

            videoPlayer.videoUrl = media.data.path

            viewPlayer.setOnClickListener {
                videoControl.show()
            }

            videoPlayer.listener = object : PickerVideoPlayer.Listener {
                override fun onPlayStateChanged(isPlaying: Boolean) {
                    viewPlayer.showWithCondition(isPlaying)
                    videoControl.showWithCondition(!isPlaying)
                }
            }

            videoPlayer.start()
        }
    }

}