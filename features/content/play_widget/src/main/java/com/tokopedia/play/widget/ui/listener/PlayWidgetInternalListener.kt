package com.tokopedia.play.widget.ui.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.WidgetInList

/**
 * Created by jegul on 21/10/20
 */
interface PlayWidgetInternalListener {

    fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView)

    fun onFocusedWidgetsChanged(focusedWidgets: List<WidgetInList>)

    fun onWidgetDetached(widget: View)
}
