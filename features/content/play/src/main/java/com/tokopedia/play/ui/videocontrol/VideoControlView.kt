package com.tokopedia.play.ui.videocontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 05/12/19
 */
class VideoControlView(
        container: ViewGroup
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video_control, container, true)
                    .findViewById(R.id.pcv_video)

    private val pcvVideo = view as PlayerControlView

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
}