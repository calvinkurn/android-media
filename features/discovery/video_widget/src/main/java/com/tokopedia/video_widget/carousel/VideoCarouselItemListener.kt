package com.tokopedia.video_widget.carousel

interface VideoCarouselItemListener {
    fun onVideoCarouselItemImpressed(videoItem: VideoCarouselDataView.VideoItem)
    fun onVideoCarouselItemClicked(videoItem: VideoCarouselDataView.VideoItem)
}