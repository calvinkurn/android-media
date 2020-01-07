package com.tokopedia.play.ui.video

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.custom.RoundedFrameLayout

/**
 * Created by jegul on 02/12/19
 */
class VideoView(container: ViewGroup) : UIView(container) {
    companion object{
        private const val ANIMATION_TRANSITION_NAME = "play_video"
    }
    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video, container, true)
                    .findViewById(R.id.rfl_video_wrapper)

    private val rflVideoWrapper = view as RoundedFrameLayout
    private val pvVideo = view.findViewById<PlayerView>(R.id.pv_video)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun setPlayer(exoPlayer: ExoPlayer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pvVideo.videoSurfaceView.transitionName = ANIMATION_TRANSITION_NAME
        }
        pvVideo.player = exoPlayer
    }

    fun setCornerRadius(cornerRadius: Float) {
        rflVideoWrapper.setCornerRadius(cornerRadius)
        rflVideoWrapper.invalidate()
        rflVideoWrapper.requestLayout()
    }
}