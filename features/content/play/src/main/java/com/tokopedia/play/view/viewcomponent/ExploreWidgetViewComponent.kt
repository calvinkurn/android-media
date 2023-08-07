package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.recom.PlayChannelRecommendationConfig
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by astidhiyaa on 28/11/22
 */
class ExploreWidgetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.view_explore_widget) {

    private val impressHolder = ImpressHolder()

    private val title = findViewById<Typography>(R.id.tv_play_explore)

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

    fun setText(value: String) {
        title.text = value
    }

    interface Listener {
        fun onExploreClicked(viewComponent: ExploreWidgetViewComponent)
        fun onExploreWidgetIconImpressed(viewComponent: ExploreWidgetViewComponent)
    }

    sealed interface Event {
        data class OnClicked(val widgetInfo: PlayChannelRecommendationConfig) : Event
        data class OnImpressed(val widgetInfo: PlayChannelRecommendationConfig): Event
    }
}
