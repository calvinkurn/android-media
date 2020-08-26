package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory
import java.util.*


enum class PlayBannerWidgetType{
    LIVE, VOD, UPCOMING, NONE
}

data class PlayBannerCarouselItemDataModel(
        val channelId: String = "",
        val channelTitle: String = "",
        val channelCreator: String = "",
        val isLive: Boolean = false,
        val isShowTotalView: Boolean = false,
        val countView: String = "",
        val coverUrl: String = "",
        val videoId: String = "",
        val videoType: String = "",
        val videoUrl: String = "",
        val isPromo: Boolean = false,
        val applink: String = "",
        val startTime: String? = "",
        val serverTime: Long = 0L,
        val widgetType: PlayBannerWidgetType = PlayBannerWidgetType.NONE,
        val partnerId: String = "",
        var remindMe: Boolean = false,
        val overlayApplink: String = ""
): BasePlayBannerCarouselModel, ImpressHolder(){
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getId(): Any {
        return channelId
    }

    override fun equalsWith(other: BasePlayBannerCarouselModel): Boolean {
        return other is PlayBannerCarouselItemDataModel
                && other.channelTitle == channelTitle
                && other.remindMe == remindMe
                && other.isLive == isLive
                && other.isPromo == isPromo
                && other.isShowTotalView == isShowTotalView
                && other.applink == applink
                && other.channelCreator == channelCreator
                && other.countView == countView
                && other.videoType == videoType
                && other.videoId == videoId
                && other.coverUrl == coverUrl
                && other.videoUrl == videoUrl
                && other.startTime == startTime
                && other.overlayApplink == overlayApplink
    }
}