package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokomart.home.constant.HomeLayoutItemState
import com.tokopedia.tokomart.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutItemUiModel

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLegoBannerData = DynamicLegoBannerDataModel(channelModel)
        return HomeLayoutItemUiModel(dynamicLegoBannerData, state)
    }
}