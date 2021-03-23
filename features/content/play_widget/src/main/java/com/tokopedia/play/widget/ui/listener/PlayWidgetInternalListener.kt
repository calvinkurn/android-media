package com.tokopedia.play.widget.ui.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by jegul on 21/10/20
 */
interface PlayWidgetInternalListener {

    fun onWidgetAttached(widgetCardsContainer: RecyclerView)

    fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView)

    fun onWidgetDetached(widget: View)
}