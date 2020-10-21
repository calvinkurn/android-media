package com.tokopedia.play.widget.ui.listener

import android.view.View

/**
 * Created by jegul on 21/10/20
 */
interface PlayWidgetViewListener {

    fun onWidgetVisibleCardsChanged(visibleCards: List<View>)
}