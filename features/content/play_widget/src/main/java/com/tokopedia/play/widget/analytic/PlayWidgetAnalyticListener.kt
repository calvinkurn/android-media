package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.analytic.medium.PlayWidgetMediumAnalyticListener
import com.tokopedia.play.widget.analytic.small.PlayWidgetSmallAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetAnalyticListener : PlayWidgetSmallAnalyticListener, PlayWidgetMediumAnalyticListener {

    fun onImpressPlayWidget(
            view: PlayWidgetView,
            widgetPositionInList: Int
    ) {}
}