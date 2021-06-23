package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokomart.home.constant.HomeLayoutItemState
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutItemUiModel

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return HomeLayoutItemUiModel(bannerDataModel, state)
    }
}