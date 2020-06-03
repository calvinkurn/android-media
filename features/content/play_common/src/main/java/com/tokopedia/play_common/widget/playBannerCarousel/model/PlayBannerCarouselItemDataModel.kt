package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory
import java.util.*


data class PlayBannerCarouselItemDataModel(
        val channelId: String = "",
        val channelTitle: String = "",
        val channelCreator: String = "",
        val isLive: Boolean = false,
        val isAutoPlay: Boolean = false,
        val isShowTotalView: Boolean = false,
        val countView: String = "",
        val coverUrl: String = "",
        val videoId: String = "",
        val videoType: String = "",
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