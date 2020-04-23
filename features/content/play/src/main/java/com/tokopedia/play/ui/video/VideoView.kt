package com.tokopedia.play.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.exoplayer2.ExoPlayer
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

    private val pvVideo = view.findViewById<VideoPlayCustom>(R.id.pv_video)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun onDestroy() {
        setPlayer(null)
        pvVideo.release()
    }

    internal fun setPlayer(exoPlayer: ExoPlayer?) {
        pvVideo.setPlayer(exoPlayer)
    }

    internal fun setOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation) {
        pvVideo.setOrientation(screenOrientation, videoOrientation)
    }
}