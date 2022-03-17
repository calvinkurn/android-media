package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface VideoCarouselInternalListener {

    fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView)

    fun onWidgetDetached(widget: View)
}