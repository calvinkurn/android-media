package com.tokopedia.video_widget.carousel

import android.view.View

interface VideoCarouselListener {
    fun onWidgetOpenAppLink(view: View, appLink: String)
}