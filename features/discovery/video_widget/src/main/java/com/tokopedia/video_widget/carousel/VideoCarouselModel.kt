package com.tokopedia.video_widget.carousel

data class VideoCarouselModel(
    val title: String,
    val itemList: List<VideoCarouselItemModel>,
    override val config: VideoCarouselConfigUiModel,
) : VideoCarouselConfigProvider
