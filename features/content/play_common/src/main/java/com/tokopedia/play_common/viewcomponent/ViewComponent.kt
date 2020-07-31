package com.tokopedia.play_common.viewcomponent

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 31/07/20
 */
abstract class ViewComponent(
        container: ViewGroup,
        @IdRes rootId: Int
) : IViewComponent {

    override val rootView: View = container.findViewById(rootId)

    protected fun <V: View> findViewById(@IdRes id: Int): V = rootView.findViewById(id)
}