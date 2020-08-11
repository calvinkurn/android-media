package com.tokopedia.play.ui.closebutton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 05/05/20
 */
class CloseButtonView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_close_button, container, true)
                    .findViewById(R.id.iv_close)

    init {
        view.setOnClickListener {
            listener.onCloseButtonClicked(this@CloseButtonView)
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    internal fun invisible() {
        view.invisible()
    }

    interface Listener {
        fun onCloseButtonClicked(view: CloseButtonView)
    }
}