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
                    if(videoPlayer.isPlaying()) hideJob()
                } else {
                    videoControl.hide()
                    cleanHideJob()
                }
            }

            videoPlayer.listener = object : PickerVideoPlayer.Listener {
                override fun onPlayStateChanged(isPlaying: Boolean) {
                    videoControl.updateCenterButton(videoPlayer.isPlaying())

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
        }, HIDE_DELAY_SCRUBBER)
    }

    override fun onCenterPauseButtonClicked() {
        videoPlayer.pause()
    }

    override fun onCenterPlayButtonClicked() {
        videoPlayer.resume()
    }

    override fun onScrubStart() {
        videoPlayer.pause()
        cleanHideJob()
    }

    override fun onScrubStop() {
        hideJob()
        Handler(Looper.getMainLooper()).postDelayed({
            videoPlayer.resume()
        }, PLAY_DELAY_SCRUBBER)
    }

    override fun onScrubMove(position: Long) {
        videoPlayer.player().seekTo(position)
    }

    companion object {
        private const val PLAY_DELAY_SCRUBBER = 1000L
        private const val HIDE_DELAY_SCRUBBER = 3000L
    }
}