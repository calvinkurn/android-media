package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse): BannerDataModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        return BannerDataModel(channelModel)
    }
}