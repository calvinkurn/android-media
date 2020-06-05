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
        val isAutoPlay: Boolean = false,
        val channelList: List<BasePlayBannerCarouselModel> = listOf()
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayBannerCarouselDataModel) return false
        if (title != other.title) return false
        if (subtitle != other.subtitle) return false
        if (textColor != other.textColor) return false
        if (seeMoreApplink != other.seeMoreApplink) return false
        if (backgroundUrl != other.backgroundUrl) return false
        if (imageUrl != other.imageUrl) return false
        if (isAutoRefresh != other.isAutoRefresh) return false
        if (isAutoRefreshTimer != other.isAutoRefreshTimer) return false
        if (channelList != other.channelList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + seeMoreApplink.hashCode()
        result = 31 * result + backgroundUrl.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + isAutoRefresh.hashCode()
        result = 31 * result + isAutoRefreshTimer
        result = 31 * result + channelList.hashCode()
        return result
    }
}