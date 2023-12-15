package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.model.error.PlayWidgetException

/**
 * Created by jegul on 13/10/20
 */
interface PlayWidgetListener :
    PlayWidgetMediumListener,
    PlayWidgetLargeListener,
    PlayWidgetCarouselView.Listener
{

    /**
     * works for medium & small type only, with PlayWidgetCoordinator
     */
    fun onWidgetShouldRefresh(view: PlayWidgetView) {}

    fun onWidgetError(view: PlayWidgetView, error: PlayWidgetException) {}
}
