package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse): BannerDataModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        return BannerDataModel(channelModel)
    }
}