package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel

object RecentPurchaseMapper {

    fun mapRecentPurchaseUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = TokoNowRecentPurchaseUiModel(
            id = response.id,
            productList = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapToRecentPurchaseUiModel(item: TokoNowRecentPurchaseUiModel, response: RecentPurchaseData): TokoNowRecentPurchaseUiModel {
        val state = TokoNowLayoutState.SHOW
        val productList = mapToProductCardUiModel(response)
        return item.copy(title = response.title, productList = productList, state = state)
    }

    private fun mapToProductCardUiModel(response: RecentPurchaseData): List<TokoNowProductCardUiModel> {
        return response.products.map {
            TokoNowProductCardUiModel(
                it.id,
                it.shop.id,
                it.stock.toInt(),
                it.parentProductId,
                createProductCardModel(it),
                TokoNowLayoutType.RECENT_PURCHASE
            )
        }
    }
    
    private fun createProductCardModel(data: RepurchaseProduct): ProductCardModel {
        return if(data.isVariant()) {
            ProductCardModel(
                productImageUrl = data.imageUrl,
                productName = data.name,
                discountPercentage = data.getDiscount(),
                slashedPrice = data.slashedPrice,
                formattedPrice = data.price,
                hasAddToCartButton = false,
                labelGroupList = mapLabelGroup(data),
                labelGroupVariantList = mapLabelGroupVariant(data),
                variant = ProductCardModel.Variant()
            )
        } else {
            ProductCardModel(
                productImageUrl = data.imageUrl,
                productName = data.name,
                discountPercentage = data.getDiscount(),
                slashedPrice = data.slashedPrice,
                formattedPrice = data.price,
                hasAddToCartButton = true,
                labelGroupList = mapLabelGroup(data),
                labelGroupVariantList = mapLabelGroupVariant(data),
                nonVariant = NonVariant(minQuantity = data.minOrder.toInt(), maxQuantity = data.stock.toInt())
            )
        }
    }
    
    private fun mapLabelGroup(response: RepurchaseProduct): List<LabelGroup> {
        return response.labelGroup.map {
            LabelGroup(it.position, it.title, it.type, it.url)
        }
    }

    private fun mapLabelGroupVariant(response: RepurchaseProduct): List<LabelGroupVariant> {
        return response.labelGroupVariant.map {
            LabelGroupVariant(it.typeVariant, it.title, it.type, it.hexColor)
        }
    }
}