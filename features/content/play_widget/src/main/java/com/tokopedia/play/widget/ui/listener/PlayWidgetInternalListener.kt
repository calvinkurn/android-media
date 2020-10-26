package com.tokopedia.play.widget.ui.listener

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 21/10/20
 */
interface PlayWidgetInternalListener {

    fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView)
}