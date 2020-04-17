package com.tokopedia.play.ui.videobackground

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 16/04/20
 */
class VideoBackgroundView(
        container: ViewGroup
) : UIView(container) {

    val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_video_background, container, true)
                    .findViewById(R.id.iv_video_background)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.gone()
    }

    internal fun setBackground(imageUrl: String) {
        Glide.with(container.context).load(imageUrl).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(@Nullable placeholder: Drawable?) {}

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                view.background = resource
            }
        })
    }
}