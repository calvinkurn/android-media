package com.tokopedia.tokopedianow.home.domain.mapper.oldrepurchase

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.model.olderpurchase.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData

object HomeRepurchaseMapper {

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
    ): List<TokoNowProductCardUiModel> {
        return response.products.mapIndexed { index, repurchaseProduct ->
            TokoNowProductCardUiModel(
                channelId,
                repurchaseProduct.id,
                repurchaseProduct.shop.id,
                repurchaseProduct.maxOrder,
                repurchaseProduct.parentProductId,
                createProductCardModel(repurchaseProduct, miniCartData),
                TokoNowLayoutType.REPURCHASE_PRODUCT,
                index + ADDITIONAL_POSITION,
                response.title
            )
        }
    }

    private fun createProductCardModel(
        product: RepurchaseProduct,
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
