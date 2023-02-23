package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play.R
import com.tokopedia.play_common.util.addImpressionListener

/**
 * @author by astidhiyaa on 28/11/22
 */
class ExploreWidgetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.view_explore_widget) {

    private val impressHolder = ImpressHolder()

    init {
        rootView.setOnClickListener {
            listener.onExploreClicked(this)
        }
        rootView.addImpressionListener(impressHolder) {
            listener.onExploreWidgetIconImpressed(this)
        }
    }

    fun setupVisibility(shouldShow: Boolean) {
        rootView.showWithCondition(shouldShow)
    }

    interface Listener {
        fun onExploreClicked(viewComponent: ExploreWidgetViewComponent)
        fun onExploreWidgetIconImpressed(viewComponent: ExploreWidgetViewComponent)
    }

    sealed interface Event {
        object OnClicked : Event
        object OnImpressed : Event
    }
}
