package com.tokopedia.play.ui.video

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
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

    internal fun setVideoOrientation(orientation: VideoOrientation) {
        pvVideo.videoOrientation = orientation
    }

    internal fun setScreenOrientation(orientation: ScreenOrientation) {
        pvVideo.screenOrientation = orientation
    }

    internal fun setBackground(imageUrl: String) {
        Glide.with(container.context).load(imageUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                pvVideo.background = resource
            }
        })
    }

    internal fun showThumbnail(shouldShow: Boolean) {
        if (shouldShow) {
            ivThumbnail.visible()
        } else {
            ivThumbnail.gone()
        }
    }

}