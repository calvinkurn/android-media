package com.tokopedia.play.widget.ui.listener

import com.tokopedia.play.widget.ui.PlayWidgetView

/**
 * Created by jegul on 13/10/20
 */
interface PlayWidgetListener :
    PlayWidgetSmallListener,
    PlayWidgetMediumListener,
    PlayWidgetLargeListener,
    PlayWidgetJumboListener {

    /**
     * works for medium & small type only, with PlayWidgetCoordinator
     */
    fun onWidgetShouldRefresh(view: PlayWidgetView) {}

}