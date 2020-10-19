package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.ui.PlayWidgetView

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetAnalyticListener : PlaySmallAnalyticListener {

    fun onImpressPlayWidget(
            view: PlayWidgetView
    ) {}
}