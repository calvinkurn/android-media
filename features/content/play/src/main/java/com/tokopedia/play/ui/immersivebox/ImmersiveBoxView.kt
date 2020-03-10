package com.tokopedia.play.ui.immersivebox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 13/12/19
 */
class ImmersiveBoxView(
        container: ViewGroup,
        listener: Listener
) : UIView(container) {
    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_immersive_box, container, true)
                    .findViewById(R.id.v_immersive_box)

    init {
        view.setOnClickListener {
            listener.onImmersiveBoxClicked(this)
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

        fun onImmersiveBoxClicked(view: ImmersiveBoxView)
    }
}