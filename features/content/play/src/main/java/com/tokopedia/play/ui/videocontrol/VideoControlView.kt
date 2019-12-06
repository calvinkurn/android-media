package com.tokopedia.play.ui.videocontrol

import android.view.LayoutInflater
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

    private val view: PlayerControlView =
            LayoutInflater.from(container.context).inflate(R.layout.view_video_control, container, true)
                    .findViewById(R.id.pcv_video)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setPlayer(exoPlayer: ExoPlayer?) {
        view.player = exoPlayer
    }
}