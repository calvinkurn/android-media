package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse): DynamicLegoBannerDataModel {
        val channelModel = mapToChannelModel(response)
        return DynamicLegoBannerDataModel(channelModel)
    }
}