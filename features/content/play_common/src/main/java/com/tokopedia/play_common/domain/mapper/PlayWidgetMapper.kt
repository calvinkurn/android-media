package com.tokopedia.play_common.domain.mapper

import com.tokopedia.play_common.domain.model.PlayWidget
import com.tokopedia.play_common.widget.playBannerCarousel.model.*
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel

object PlayWidgetMapper {
    fun mapperToPlayBannerCarouselDataModel(playWidget: PlayWidget): PlayBannerCarouselDataModel{
        val list = mutableListOf<BasePlayBannerCarouselModel>()
        if(playWidget.meta.overlayImage.isNotBlank())
        list.add(PlayBannerCarouselOverlayImageDataModel(
                applink = playWidget.meta.overlayImageApplink ?: "",
                imageUrl = playWidget.meta.overlayImage
        ))

        list.addAll(
                playWidget.data.map {
                    if(it.isBanner()){
                        PlayBannerCarouselBannerDataModel(applink = it.appLink, imageUrl = it.backgroundURL)
                    } else {
                        PlayBannerCarouselItemDataModel(
                                channelTitle = it.title,
                                applink = it.appLink,
                                channelCreator = it.partner.name,
                                countView = it.stats.view.formatted,
                                isLive = it.video.isLive,
                                isShowTotalView = it.video.isShowTotalView,
                                isPromo = it.config.hasPromo,
                                widgetType = when(it.widgetType){
                                    "WATCH_AGAIN" -> PlayBannerWidgetType.VOD
                                    "COMING_SOON" -> PlayBannerWidgetType.UPCOMING
                                    "LIVE" -> PlayBannerWidgetType.LIVE
                                    else -> PlayBannerWidgetType.NONE
                                },
                                serverTime = 0,
                                startTime = it.startTime,
                                videoUrl = it.video.streamSource,
                                coverUrl = it.video.coverUrl,
                                channelId = it.id,
                                videoId = it.video.id,
                                videoType = it.video.type,
                                remindMe = it.config.isReminderSet
                        )
                    }
                }
        )
        return PlayBannerCarouselDataModel(
                title = playWidget.meta.widgetTitle,
                backgroundUrl = playWidget.meta.widgetBackground,
                isAutoPlay = playWidget.meta.autoplay,
                isAutoPlayAmount = playWidget.meta.autoplayAmount,
                isAutoRefresh = playWidget.meta.isAutoRefresh,
                isAutoRefreshTimer = playWidget.meta.autoRefreshTimer,
                seeMoreApplink = playWidget.meta.buttonApplink,
                imageUrl = playWidget.meta.overlayImage,
                serverTimeOffset = playWidget.meta.serverTimeOffset,
                channelList = list,
                gradients = playWidget.meta.gradient,
                durationPlayWithData = playWidget.meta.maxAutoplayCell
        )
    }
}