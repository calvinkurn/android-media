package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse

object SliderBannerMapper {
    fun mapSliderBannerModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return HomeLayoutItemUiModel(bannerDataModel, state)
    }
}