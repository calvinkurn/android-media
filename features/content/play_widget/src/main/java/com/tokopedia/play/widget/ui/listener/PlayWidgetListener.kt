package com.tokopedia.play.widget.ui.listener

import android.view.View

/**
 * Created by jegul on 13/10/20
 */
interface PlayWidgetListener : PlayWidgetMediumListener {

    fun onWidgetShouldRefresh(view: View)
}