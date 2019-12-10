package com.tokopedia.play.ui.loading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 09/12/19
 */
class LoadingView(
        container: ViewGroup
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_loading, container, true)
                    .findViewById(R.id.fl_loading)

    override val containerId: Int = view.id

    private val ivLoading = view.findViewById<ImageView>(R.id.iv_loading)

    override fun show() {
        if (ivLoading.drawable == null) {
            Glide.with(ivLoading.context)
                    .asGif()
                    .load(R.drawable.ic_play_loading)
                    .into(ivLoading)
        }
        view.show()
    }

    override fun hide() {
        view.hide()
    }
}