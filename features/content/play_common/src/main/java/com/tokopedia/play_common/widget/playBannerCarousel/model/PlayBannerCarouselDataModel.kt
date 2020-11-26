package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel

data class PlayBannerCarouselDataModel (
        val title: String = "",
        val subtitle: String = "",
        val seeMoreApplink: String = "",
        val backgroundUrl: String = "",
        val gradients: List<String> = listOf("#ffffff"),
        val imageUrl: String = "",
        val isAutoRefresh: Boolean = false,
        val isAutoRefreshTimer: Int = 0,
        val isAutoPlayAmount: Int = 1,
        val isAutoPlay: Boolean = false,
        val channelList: List<BasePlayBannerCarouselModel> = listOf(),
        val durationPlayWithData: Int = 25,
        val durationDelayStartVideo: Int = 3
)