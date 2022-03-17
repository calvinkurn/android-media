package com.tokopedia.video_widget.carousel

data class VideoCarouselItemModel(
    val id: Int,
    val videoURL: String,
    val imageURL: String,
    val title: String,
    val subTitle: String,
    val applink: String,
)