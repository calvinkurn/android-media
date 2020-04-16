package com.tokopedia.play.ui.videocontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.TimeBar
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 05/12/19
 */
class VideoControlView(
        container: ViewGroup,
        private val listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video_control, container, true)
                    .findViewById(R.id.pcv_video)

    private val pcvVideo = view as PlayerControlView
    private val timeBar = pcvVideo.findViewById<DefaultTimeBar>(com.google.android.exoplayer2.ui.R.id.exo_progress)

    init {
        timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }

            override fun onScrubStart(timeBar: TimeBar, position: Long) {
                listener.onStartSeeking(this@VideoControlView)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                listener.onEndSeeking(this@VideoControlView)
            }
        })
    }

    override val containerId: Int = view.id

    override fun show() {
        pcvVideo.show()
    }

    override fun hide() {
        pcvVideo.hide()
    }

    fun onDestroy() {
        pcvVideo.player = null
    }

    fun setPlayer(exoPlayer: ExoPlayer?) {
        pcvVideo.player = exoPlayer
    }

    interface Listener {

        fun onStartSeeking(view: VideoControlView)
        fun onEndSeeking(view: VideoControlView)
    }
}