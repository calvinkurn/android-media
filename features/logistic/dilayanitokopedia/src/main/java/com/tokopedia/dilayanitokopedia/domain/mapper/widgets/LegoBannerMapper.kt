package com.tokopedia.dilayanitokopedia.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.common.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.ChannelMapper.mapToChannelModel
import com.tokopedia.dilayanitokopedia.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel

object LegoBannerMapper {

    fun mapLegoBannerDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLegoBannerData = DynamicLegoBannerDataModel(channelModel)
        return HomeLayoutItemUiModel(dynamicLegoBannerData, state, response.groupId)
    }
}
