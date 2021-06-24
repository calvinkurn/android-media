package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokomart.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse): DynamicLegoBannerDataModel {
        val channelModel = mapToChannelModel(response)
        return DynamicLegoBannerDataModel(channelModel)
    }
}