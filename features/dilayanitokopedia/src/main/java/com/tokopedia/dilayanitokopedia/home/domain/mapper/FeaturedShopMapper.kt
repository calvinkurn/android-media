package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel

object FeaturedShopMapper {

    fun mapToFeaturedShop(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val featuredShopDataModel = FeaturedShopDataModel(
            channelModel = channelModel,
            state = FeaturedShopDataModel.STATE_LOADING,
            page = FeaturedShopDataModel.PAGE_OS
        )
        return HomeLayoutItemUiModel(featuredShopDataModel, state)
    }

}