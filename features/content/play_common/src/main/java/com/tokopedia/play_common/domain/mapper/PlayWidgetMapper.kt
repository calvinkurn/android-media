package com.tokopedia.play_common.domain.mapper

import com.tokopedia.play_common.domain.model.PlayWidget
import com.tokopedia.play_common.widget.playBannerCarousel.helper.DateHelper
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel

object PlayWidgetMapper {
    fun mapperToPlayBannerCarouselDataModel(playWidget: PlayWidget): PlayBannerCarouselDataModel{
        val list = mutableListOf<BasePlayBannerCarouselModel>()
        list.add(PlayBannerCarouselEmptyDataModel())

        list.addAll(
                playWidget.data.map {
                    if(it.isBanner()){
                        PlayBannerCarouselBannerDataModel(applink = it.appLink, imageUrl = it.backgroundURL)
                    } else {
                        PlayBannerCarouselItemDataModel(
                                channelTitle = it.title,
                                isAutoPlay = playWidget.meta.autoplay,
                                applink = it.appLink,
                                channelCreator = it.partner.name,
                                countView = it.stats.view.formatted,
                                endTime = DateHelper.getExpiredTime(it.endTime),
                                isLive = it.video.isLive,
                                isShowTotalView = it.video.isShowTotalView,
                                promoUrl = "",
                                serverTime = 0,
                                startTime = DateHelper.getExpiredTime(it.startTime),
                                videoUrl = it.video.streamSource,
                                coverUrl = it.video.coverUrl,
                                channelId = it.id,
                                videoId = it.video.id,
                                videoType = it.video.type
                        )
                    }
                }
        )
        return PlayBannerCarouselDataModel(
                title = playWidget.meta.widgetTitle,
                backgroundUrl = playWidget.meta.widgetBackground,
                isAutoRefresh = playWidget.meta.isAutoRefresh,
                isAutoRefreshTimer = playWidget.meta.autoRefreshTimer,
                seeMoreApplink = playWidget.meta.buttonApplink,
                channelList = list
        )
    }
}