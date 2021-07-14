package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelGrid
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
        channel.channelGrids.forEachIndexed { index, channelGrid ->
            if (index != 10) {
                if (channelGrid.isOutOfStock || channelGrid.stock == 0) {
                    return@forEachIndexed
                }
                val model = getVariantNoVariantProduct(
                    channelGrid = channelGrid,
                    model = getHomeProductCard(channelGrid, "")
                )
                productRecom.add(model)
            } else {
                return productRecom
            }
        }
        return productRecom
    }

    private fun getHomeProductCard(channelGrid: ChannelGrid, shopId: String): HomeProductCardUiModel {
        return HomeProductCardUiModel(
            id = channelGrid.id,
            parentProductId = channelGrid.parentProductId,
            shopId = shopId
        )
    }

    private fun getVariantNoVariantProduct(channelGrid: ChannelGrid, model: HomeProductCardUiModel): HomeProductCardUiModel {
        return if (channelGrid.parentProductId == NO_VARIANT_PARENT_PRODUCT_ID) {
            mapVariantProduct(channelGrid, model)
        } else {
            mapNonVariantProduct(channelGrid, model)
        }
    }

    private fun mapVariantProduct(channelGrid: ChannelGrid, model: HomeProductCardUiModel):  HomeProductCardUiModel{
        return model.copy(
            productCardModel = ChannelModelMapper.mapToProductCardModel(channelGrid).copy(
                nonVariant = ProductCardModel.NonVariant(
                    minQuantity = channelGrid.minOrder,
                    maxQuantity = channelGrid.stock
                )
            )
        )
    }

    private fun mapNonVariantProduct(channelGrid: ChannelGrid, model: HomeProductCardUiModel):  HomeProductCardUiModel{
        return model.copy(
            productCardModel = ChannelModelMapper.mapToProductCardModel(channelGrid).copy(
                variant = ProductCardModel.Variant()
            )
        )
    }
}

