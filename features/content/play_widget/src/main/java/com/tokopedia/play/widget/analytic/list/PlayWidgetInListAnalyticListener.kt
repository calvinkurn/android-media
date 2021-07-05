package com.tokopedia.play.widget.analytic.list

import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.analytic.list.small.PlayWidgetInListSmallAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListAnalyticListener : PlayWidgetInListSmallAnalyticListener, PlayWidgetInListMediumAnalyticListener {

    fun onImpressPlayWidget(
            view: PlayWidgetView,
            item: PlayWidgetUiModel,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    )
}