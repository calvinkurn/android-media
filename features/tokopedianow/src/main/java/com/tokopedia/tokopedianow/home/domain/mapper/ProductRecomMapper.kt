package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.common.constant.ConstantKey.NO_VARIANT_PARENT_PRODUCT_ID
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
        val productRecom :MutableList<HomeProductCardUiModel> = mutableListOf()
        channel.channelGrids.map { grid ->
            var model = HomeProductCardUiModel(
                id = grid.id,
                parentProductId = grid.parentProductId
            )
            if (grid.parentProductId == NO_VARIANT_PARENT_PRODUCT_ID) {
                model = model.copy(
                    productCardModel = ChannelModelMapper.mapToProductCardModel(grid).copy(
                        nonVariant = ProductCardModel.NonVariant(
                            minQuantity = grid.minOrder,
                            maxQuantity = grid.stock
                        )
                    )
                )
            } else {
                model = model.copy(
                    productCardModel = ChannelModelMapper.mapToProductCardModel(grid).copy(
                        variant = ProductCardModel.Variant()
                    )
                )
            }
            productRecom.add(model)
        }
        return productRecom
    }

}

