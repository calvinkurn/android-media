package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal interface VideoCarouselInternalListener {
    fun playVideo(container: RecyclerView)
    fun stopVideo()

    fun onWidgetCardsScrollChanged(container: RecyclerView)
    fun onWidgetDetached(widget: View)
}