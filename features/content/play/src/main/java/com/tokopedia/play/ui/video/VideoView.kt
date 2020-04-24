package com.tokopedia.play.ui.video

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 02/12/19
 */
class VideoView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video, container, true)
                    .findViewById(R.id.cl_video_view)

    private val pvVideo = view.findViewById<PlayerView>(R.id.pv_video)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun onDestroy() {
        setPlayer(null)
    }

    internal fun setPlayer(exoPlayer: ExoPlayer?) {
        pvVideo.player = exoPlayer
    }

    internal fun setOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        pvVideo.resizeMode = if (videoOrientation.isHorizontal) {
            when {
                screenOrientation.isLandscape -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                else -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            }
        } else AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        val lParams = pvVideo.layoutParams as FrameLayout.LayoutParams
        lParams.gravity =
                if (videoOrientation.isHorizontal && !screenOrientation.isLandscape) Gravity.NO_GRAVITY
                else Gravity.CENTER

        pvVideo.layoutParams = lParams
    }
}