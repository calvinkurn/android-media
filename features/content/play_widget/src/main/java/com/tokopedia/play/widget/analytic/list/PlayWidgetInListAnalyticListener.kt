package com.tokopedia.play.widget.analytic.list

import com.tokopedia.play.widget.analytic.list.carousel.PlayWidgetInListCarouselAnalyticListener
import com.tokopedia.play.widget.analytic.list.large.PlayWidgetInListLargeAnalyticListener
import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListAnalyticListener :
    PlayWidgetInListMediumAnalyticListener,
    PlayWidgetInListLargeAnalyticListener,
    PlayWidgetInListCarouselAnalyticListener
{

    fun onImpressPlayWidget(
            view: PlayWidgetView,
            item: PlayWidgetUiModel,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {}
}
