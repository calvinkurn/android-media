package com.tokopedia.video_widget.carousel

interface VideoCarouselItemListener {
    fun onVideoCarouselItemImpressed(position: Int)
    fun onVideoCarouselItemClicked(position: Int)
}