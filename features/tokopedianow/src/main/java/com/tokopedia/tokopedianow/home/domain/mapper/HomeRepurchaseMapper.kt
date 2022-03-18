package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData2
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.DEFAULT_QUANTITY
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
        miniCartData: MiniCartSimplifiedData2? = null
    ): TokoNowRepurchaseUiModel {
        val state = TokoNowLayoutState.SHOW
        val productList = mapToProductCardUiModel(response, miniCartData)
        return item.copy(title = response.title, productList = productList, state = state)
    }

    private fun mapToProductCardUiModel(
        response: RepurchaseData,
        miniCartData: MiniCartSimplifiedData2? = null
    ): List<TokoNowProductCardUiModel> {
        return response.products.map {
            TokoNowProductCardUiModel(
                it.id,
                it.shop.id,
                it.maxOrder,
                it.parentProductId,
                createProductCardModel(it, miniCartData),
                TokoNowLayoutType.REPURCHASE_PRODUCT
            )
        }
    }
    
    private fun createProductCardModel(
        product: RepurchaseProduct,
        miniCartData: MiniCartSimplifiedData2? = null
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
                    minQuantity = product.minOrder,
                    maxQuantity = product.maxOrder
                )
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