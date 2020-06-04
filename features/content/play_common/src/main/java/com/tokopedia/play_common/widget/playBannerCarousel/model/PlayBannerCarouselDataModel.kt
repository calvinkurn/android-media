package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel

data class PlayBannerCarouselDataModel (
        val title: String = "",
        val subtitle: String = "",
        val textColor: String = "",
        val seeMoreApplink: String = "",
        val backgroundUrl: String = "",
        val imageUrl: String = "",
        val isAutoRefresh: Boolean = false,
        val isAutoRefreshTimer: Int = 0,
        val channelList: List<BasePlayBannerCarouselModel> = listOf()
)