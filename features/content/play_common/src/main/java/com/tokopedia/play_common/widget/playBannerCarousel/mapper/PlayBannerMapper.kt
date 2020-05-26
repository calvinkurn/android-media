package com.tokopedia.play_common.widget.playBannerCarousel.mapper

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselSeeMoreDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel

object PlayBannerMapper {
    fun mapToDataModel(dataModel: PlayBannerCarouselDataModel): List<BasePlayBannerCarouselModel>{
        val list = mutableListOf<BasePlayBannerCarouselModel>()
        list.add(PlayBannerCarouselEmptyDataModel())
        list.addAll(dataModel.channelList)
        if(dataModel.seeMoreApplink.isNotBlank()){
            list.add(PlayBannerCarouselSeeMoreDataModel())
        }
        return list
    }
}