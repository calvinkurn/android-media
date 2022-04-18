package com.tokopedia.media.preview.ui.activity.pagers.views

import android.content.Context
import android.os.Handler
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.media.R
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.media.preview.ui.uimodel.PreviewUiModel
import android.os.Looper

class VideoPreview(
    private val context: Context,
    val videoPlayer: PickerVideoPlayer
) : BasePagerPreview, VideoControlView.Listener {

    override val layout: Int
        get() = R.layout.view_item_preview_video

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var videoControl: VideoControlView

    override fun setupView(media: PreviewUiModel): View {
        return rootLayoutView(context).also {
            val viewPlayer = it.findViewById<PlayerView>(R.id.video_preview)
            videoControl = it.findViewById(R.id.video_control)

            viewPlayer.player = videoPlayer.player()
            videoControl.player = videoPlayer.player()

            videoPlayer.videoUrl = media.data.path

            it.setOnClickListener {
                if(!videoControl.isVisible) {
                    videoControl.show()
                    hideJob()
                } else {
                    videoControl.hide()
                    cleanHideJob()
                }
            }

            videoPlayer.listener = object : PickerVideoPlayer.Listener {
                override fun onPlayStateChanged(isPlaying: Boolean) {
                    videoControl.updateCenterButton(!videoPlayer.isPlaying())

                    if(videoPlayer.isPlaying()){
                        hideJob()
                    } else {
                        cleanHideJob()
                    }
                }
            }

            videoControl.setListener(this)
        }
    }

    fun cleanHideJob(){
        handler.removeCallbacksAndMessages(null)
    }

    fun hideJob(){
        handler.postDelayed({
            videoControl.hide()
        }, 3000)
    }

    override fun onCenterControlButtonClicked() {
        if(videoPlayer.isPlaying()) videoPlayer.pause() else videoPlayer.resume()
    }

    override fun onScrubStart() {
        videoPlayer.pause()
        cleanHideJob()
    }

    override fun onScrubStop() {
        hideJob()
        Handler(Looper.getMainLooper()).postDelayed({
            videoPlayer.resume()
        },1000)
    }

    override fun onScrubMove(position: Long) {
        videoPlayer.player().seekTo(position)
    }
}