package com.tokopedia.dilayanitokopedia.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.common.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.domain.mapper.widgets.ChannelMapper.mapToChannelModel
import com.tokopedia.dilayanitokopedia.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.MixLeftDataModel

object LeftCarouselMapper {

    fun mapToLeftCarousel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val mixLeftDataModel = MixLeftDataModel(channelModel)
        return HomeLayoutItemUiModel(mixLeftDataModel, state, response.groupId)
    }
}
