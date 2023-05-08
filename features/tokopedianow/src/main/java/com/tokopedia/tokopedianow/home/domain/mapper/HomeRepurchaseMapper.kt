package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseProductUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object HomeRepurchaseMapper {

    fun mapRepurchaseUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = TokoNowRepurchaseUiModel(
            id = response.id,
            productList = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapToRepurchaseUiModel(
        item: TokoNowRepurchaseUiModel,
        response: RepurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ): TokoNowRepurchaseUiModel {
        val state = TokoNowLayoutState.SHOW
        val productList = mapToProductCardUiModel(item.id, response, miniCartData)
        return item.copy(title = response.title, productList = productList, state = state)
    }

    private fun mapToProductCardUiModel(
        channelId: String,
        response: RepurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ): List<TokoNowRepurchaseProductUiModel> {
        return response.products.mapIndexed { index, product ->
            val quantity = getAddToCartQuantity(product.id, miniCartData)

            TokoNowRepurchaseProductUiModel(
                channelId = channelId,
                productId = product.id,
                parentId = product.parentProductId,
                shopId = product.shop.id,
                imageUrl = product.imageUrl,
                name = product.name,
                availableStock = product.stock,
                minOrder = product.minOrder,
                maxOrder = product.maxOrder,
                orderQuantity = quantity,
                discount = product.getDiscount(),
                slashPrice = product.slashedPrice,
                formattedPrice = product.price,
                labelGroupList = mapLabelGroup(product),
                isVariant = product.isVariant(),
                position = index + ADDITIONAL_POSITION,
                headerName = response.title,
                needToShowQuantityEditor = true
            )
        }
    }

    private fun mapLabelGroup(response: RepurchaseProduct): List<LabelGroup> {
        return response.labelGroup.map {
            LabelGroup(it.position, it.title, it.type, it.url)
        }
    }
}
