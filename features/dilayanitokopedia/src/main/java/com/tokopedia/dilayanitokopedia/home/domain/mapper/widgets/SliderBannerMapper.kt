package com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets

import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.BannerDataModel

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return HomeLayoutItemUiModel(bannerDataModel, state, response.groupId)
    }
}
