package com.tokopedia.dilayanitokopedia.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.MixTopDataModel

object TopCarouselMapper {
    fun mapTopCarouselModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val mixTopData = MixTopDataModel(channelModel)
        return HomeLayoutItemUiModel(mixTopData, state, response.groupId)
    }
}

