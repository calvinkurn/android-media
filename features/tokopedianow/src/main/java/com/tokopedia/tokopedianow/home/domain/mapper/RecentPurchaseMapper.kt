package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRecentPurchaseUiModel

object RecentPurchaseMapper {

    fun mapRecentPurchaseUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = HomeRecentPurchaseUiModel(
            id = response.id,
            productList = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapToRecentPurchaseUiModel(item: HomeRecentPurchaseUiModel, response: RecentPurchaseData): HomeRecentPurchaseUiModel {
        val state = TokoNowLayoutState.SHOW
        val productList = mapToProductCardUiModel(response)
        return item.copy(title = response.title, productList = productList, state = state)
    }

    private fun mapToProductCardUiModel(response: RecentPurchaseData): List<HomeProductCardUiModel> {
        return response.products.map {
            HomeProductCardUiModel(
                it.shop.id,
                it.id,
                it.stock.toInt(),
                it.parentProductId,
                createProductCardModel(it),
                HomeLayoutType.RECENT_PURCHASE
            )
        }
    }
    
    private fun createProductCardModel(data: Product): ProductCardModel {
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
    
    private fun mapLabelGroup(response: Product): List<LabelGroup> {
        return response.labelGroup.map {
            LabelGroup(it.position, it.title, it.type, it.url)
        }
    }

    private fun mapLabelGroupVariant(response: Product): List<LabelGroupVariant> {
        return response.labelGroupVariant.map {
            LabelGroupVariant(it.typeVariant, it.title, it.type, it.hexColor)
        }
    }
}