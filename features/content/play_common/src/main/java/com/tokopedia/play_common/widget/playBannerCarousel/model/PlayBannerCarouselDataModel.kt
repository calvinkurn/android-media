package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory
import java.util.*

data class PlayBannerCarouselDataModel (
        val title: String = "",
        val subtitle: String = "",
        val textColor: String = "",
        val seeMoreApplink: String = "",
        val backgroundUrl: String = "",
        val isAutoRefresh: Boolean = false,
        val isAutoRefreshTimer: Int = 0,
        val gradientColors: List<String> = listOf("#ffffff"),
        val playBannerCarouselAddStoryDataModel: PlayBannerCarouselAddStoryDataModel? = null,
        val channelList: List<PlayBannerCarouselItemDataModel> = listOf()
)

data class PlayBannerCarouselItemDataModel(
        val channelTitle: String = "",
        val channelCreator: String = "",
        val isLive: Boolean = false,
        val isAutoPlay: Boolean = false,
        val isShowTotalView: Boolean = false,
        val countView: String = "",
        val videoUrl: String = "",
        val promoUrl: String = "",
        val applink: String = "",
        val startTime: Date? = null,
        val endTime: Date? = null,
        val serverTime: Long = 0L
): BasePlayBannerCarouselModel, ImpressHolder(){
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}