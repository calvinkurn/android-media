package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object MixLeftCarouselMapper {

    fun mapToMixLeftCarousel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel  {
        val channelModel = mapToChannelModel(response)
        val mixLeftDataModel = MixLeftDataModel(channelModel)
        return HomeLayoutItemUiModel(mixLeftDataModel, state)
    }
}