package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView

/**
 * Created by jegul on 13/10/20
 */
interface PlayWidgetListener :
    PlayWidgetSmallListener,
    PlayWidgetMediumListener,
    PlayWidgetLargeListener,
    PlayWidgetJumboListener,
    PlayWidgetCarouselView.Listener
{

    /**
     * works for medium & small type only, with PlayWidgetCoordinator
     */
    fun onWidgetShouldRefresh(view: PlayWidgetView) {}

}
