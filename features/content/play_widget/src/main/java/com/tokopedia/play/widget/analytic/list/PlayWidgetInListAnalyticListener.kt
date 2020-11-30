package com.tokopedia.play.widget.analytic.list

import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.analytic.list.small.PlayWidgetInListSmallAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListAnalyticListener : PlayWidgetInListSmallAnalyticListener, PlayWidgetInListMediumAnalyticListener {

    fun onImpressPlayWidget(
            view: PlayWidgetView,
            widgetPositionInList: Int
    )
}