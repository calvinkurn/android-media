package com.tokopedia.play.ui.gradientbg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 10/01/20
 */
class GradientBackgroundView(container: ViewGroup) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_mini_gradient_background, container, true)
                    .findViewById(R.id.cl_play_gradient)

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }
}