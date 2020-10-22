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
                playWidget.data.map {playWidgetItem ->
                    if(playWidgetItem.isBanner()){
                        PlayBannerCarouselBannerDataModel(applink = playWidgetItem.appLink, imageUrl = playWidgetItem.backgroundURL)
                    } else {
                        PlayBannerCarouselItemDataModel(
                                channelTitle = playWidgetItem.title,
                                applink = playWidgetItem.appLink,
                                channelCreator = playWidgetItem.partner.name,
                                countView = playWidgetItem.stats.view.formatted,
                                isLive = playWidgetItem.video.isLive,
                                isShowTotalView = playWidgetItem.video.isShowTotalView,
                                isPromo = playWidgetItem.config.hasPromo,
                                widgetType = when(playWidgetItem.widgetType){
                                    "WATCH_AGAIN" -> PlayBannerWidgetType.VOD
                                    "COMING_SOON" -> PlayBannerWidgetType.UPCOMING
                                    "LIVE" -> PlayBannerWidgetType.LIVE
                                    else -> PlayBannerWidgetType.NONE
                                },
                                serverTime = 0,
                                startTime = playWidgetItem.startTime,
                                videoUrl = playWidgetItem.video.streamSource,
                                coverUrl = playWidgetItem.video.coverUrl,
                                channelId = playWidgetItem.id,
                                videoId = playWidgetItem.video.id,
                                videoType = playWidgetItem.video.type,
                                remindMe = playWidgetItem.config.isReminderSet,
                                partnerId = playWidgetItem.partner.id
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
                channelList = list,
                gradients = playWidget.meta.gradient,
                durationPlayWithData = playWidget.meta.maxAutoplayCell
        )
    }
}