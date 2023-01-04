package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.MixLeftDataModel

object LeftCarouselMapper {

    fun mapToLeftCarousel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val mixLeftDataModel = MixLeftDataModel(channelModel)
        return HomeLayoutItemUiModel(mixLeftDataModel, state)
    }
}
