package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play.R

/**
 * @author by astidhiyaa on 28/11/22
 */
class ExploreWidgetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.view_explore_widget) {

    init {
        rootView.setOnClickListener {
            listener.onExploreClicked(this)
        }
    }

    fun setupVisibility(shouldShow: Boolean) {
        rootView.showWithCondition(shouldShow)
    }

    interface Listener {
        fun onExploreClicked(viewComponent: ExploreWidgetViewComponent)
    }
}
