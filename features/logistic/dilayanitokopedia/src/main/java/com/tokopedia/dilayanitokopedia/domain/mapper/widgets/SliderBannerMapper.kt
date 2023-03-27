package com.tokopedia.dilayanitokopedia.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.common.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.BannerDataModel

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return HomeLayoutItemUiModel(bannerDataModel, state, response.groupId)
    }
}
