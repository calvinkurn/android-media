package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.analytic.carousel.PlayWidgetCarouselAnalyticListener
import com.tokopedia.play.widget.analytic.large.PlayWidgetLargeAnalyticListener
import com.tokopedia.play.widget.analytic.medium.PlayWidgetMediumAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetAnalyticListener :
    PlayWidgetMediumAnalyticListener,
    PlayWidgetLargeAnalyticListener,
    PlayWidgetCarouselAnalyticListener
{

    fun onImpressPlayWidget(
        view: PlayWidgetView,
        item: PlayWidgetUiModel,
        widgetPositionInList: Int
    ) {}
}
