package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

object ProductRecomMapper {
    fun mapProductRecomDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val productRecom = HomeProductRecomUiModel(channelModel.id, convertDataToProductData(channelModel))
        return HomeLayoutItemUiModel(productRecom, state)
    }

    private fun convertDataToProductData(channel: ChannelModel): List<HomeProductCardUiModel> {
        val list :MutableList<HomeProductCardUiModel> = mutableListOf()
        channel.channelGrids.map { grid ->
            list.add(HomeProductCardUiModel(
                id = grid.id,
                productCardModel = ChannelModelMapper.mapToProductCardModel(grid),
                parentProductId = grid.parentProductId
            ))
        }
        return list
    }

}

