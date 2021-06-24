package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLegoBannerData = DynamicLegoBannerDataModel(channelModel)
        return HomeLayoutItemUiModel(dynamicLegoBannerData, state)
    }
}