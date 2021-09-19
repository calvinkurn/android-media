package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.*
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity

object RecentPurchaseMapper {

    fun mapRecentPurchaseUiModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val uiModel = TokoNowRecentPurchaseUiModel(
            id = response.id,
            productList = emptyList(),
            state = TokoNowLayoutState.LOADING
        )
        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun mapToRecentPurchaseUiModel(
        item: TokoNowRecentPurchaseUiModel,
        response: RecentPurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ): TokoNowRecentPurchaseUiModel {
        val state = TokoNowLayoutState.SHOW
        val productList = mapToProductCardUiModel(response, miniCartData)
        return item.copy(title = response.title, productList = productList, state = state)
    }

    private fun mapToProductCardUiModel(
        response: RecentPurchaseData,
        miniCartData: MiniCartSimplifiedData? = null
    ): List<TokoNowProductCardUiModel> {
        return response.products.map {
            TokoNowProductCardUiModel(
                it.id,
                it.shop.id,
                it.stock.toInt(),
                it.parentProductId,
                createProductCardModel(it, miniCartData),
                TokoNowLayoutType.RECENT_PURCHASE
            )
        }
    }
    
    private fun createProductCardModel(
        product: Product,
        miniCartData: MiniCartSimplifiedData? = null
    ): ProductCardModel {
        val quantity = getAddToCartQuantity(product.id, miniCartData)

        return if(product.isVariant()) {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasAddToCartButton = false,
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                variant = ProductCardModel.Variant(quantity)
            )
        } else {
            ProductCardModel(
                productImageUrl = product.imageUrl,
                productName = product.name,
                discountPercentage = product.getDiscount(),
                slashedPrice = product.slashedPrice,
                formattedPrice = product.price,
                hasAddToCartButton = quantity == DEFAULT_QUANTITY,
                labelGroupList = mapLabelGroup(product),
                labelGroupVariantList = mapLabelGroupVariant(product),
                nonVariant = NonVariant(
                    quantity = quantity,
                    minQuantity = product.minOrder.toInt(),
                    maxQuantity = product.stock.toInt()
                )
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