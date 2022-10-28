package com.tokopedia.video_widget.carousel

interface InspirationVideoCarouselListener {
    fun onInspirationVideoCarouselProductImpressed(videoItem: VideoCarouselDataView.VideoItem)

    fun onInspirationVideoCarouselProductClicked(videoItem: VideoCarouselDataView.VideoItem)
}