package com.tokopedia.play_common.viewcomponent

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by jegul on 31/07/20
 */
abstract class ViewComponent(
        container: ViewGroup,
        @IdRes rootId: Int
) : IViewComponent {

    override val rootView: View = container.findViewById(rootId)

    protected fun <V: View> findViewById(@IdRes id: Int): V = rootView.findViewById(id)

    open fun show() {
        rootView.show()
    }

    open fun hide() {
        rootView.hide()
    }
}