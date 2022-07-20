package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 08/12/21
 */
class KebabMenuViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    init {
        rootView.setOnClickListener {
            listener.onKebabMenuClick(this@KebabMenuViewComponent)
        }
    }

    interface Listener{
        fun onKebabMenuClick(view: KebabMenuViewComponent)
    }
}