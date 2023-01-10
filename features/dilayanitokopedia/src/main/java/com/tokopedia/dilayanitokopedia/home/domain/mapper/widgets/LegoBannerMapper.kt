package com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.ChannelMapper.mapToChannelModel
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLegoBannerData = DynamicLegoBannerDataModel(channelModel)
        return HomeLayoutItemUiModel(dynamicLegoBannerData, state, response.groupId)
    }
}
