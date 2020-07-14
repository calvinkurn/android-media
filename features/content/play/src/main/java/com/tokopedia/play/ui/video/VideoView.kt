package com.tokopedia.play.ui.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/12/19
 */
class VideoView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video, container, true)
                    .findViewById(R.id.cl_video_view)

    private val pvVideo = view.findViewById<VideoPlayCustom>(R.id.pv_video)
    private val ivThumbnail = view.findViewById<ImageView>(R.id.iv_thumbnail)

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

    internal fun setThumbnail() {
        pvVideo.textureView?.bitmap?.let {
            ivThumbnail.setImageBitmap(it)
        }
    }

    internal fun showThumbnail(shouldShow: Boolean) {
        if (shouldShow) {
            ivThumbnail.visible()
        } else {
            ivThumbnail.gone()
        }
    }

}