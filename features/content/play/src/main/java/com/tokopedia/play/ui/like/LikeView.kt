package com.tokopedia.play.ui.like

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/12/19
 */
class LikeView(container: ViewGroup, listener: Listener) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_like, container, true)
                    .findViewById(R.id.iv_like)

    init {
        view.setOnClickListener {
            listener.onLikeClicked(this)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    interface Listener {

        fun onLikeClicked(view: LikeView)
    }
}