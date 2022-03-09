package com.tokopedia.shop_widget.common.util

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.LabelGroupUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.unifycomponents.UnifyButton

object ProductCardMapper {
    const val LABEL_POSITION_INTEGRITY = "integrity"

    private fun mapToProductCardLabelGroup(labelGroupUiModel: LabelGroupUiModel): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
            position = labelGroupUiModel.position,
            title = labelGroupUiModel.title,
            type = labelGroupUiModel.type,
            imageUrl = labelGroupUiModel.url
        )
    }

    fun mapToProductCardCampaignModel(isHasAddToCartButton: Boolean, hasThreeDots: Boolean, productCardUiModel: ProductCardUiModel): ProductCardModel {
        val discountWithoutPercentageString = productCardUiModel.discountPercentage?.replace("%", "").orEmpty()
        val discountPercentage = if (discountWithoutPercentageString == "0") { "" } else { "$discountWithoutPercentageString%" }
        val freeOngkirObject = ProductCardModel.FreeOngkir(
            isActive = productCardUiModel.isShowFreeOngkir,
            imageUrl = productCardUiModel.freeOngkirPromoIcon.orEmpty()
        )
        return ProductCardModel(
            productImageUrl = productCardUiModel.imageUrl.orEmpty(),
            productName = productCardUiModel.name.orEmpty(),
            discountPercentage = discountPercentage.takeIf { !productCardUiModel.hideGimmick }.orEmpty(),
            slashedPrice = productCardUiModel.originalPrice.orEmpty().takeIf { !productCardUiModel.hideGimmick }.orEmpty(),
            formattedPrice = productCardUiModel.displayedPrice.orEmpty(),
            countSoldRating = if (productCardUiModel.rating != 0.0) productCardUiModel.rating.toString() else "",
            freeOngkir = freeOngkirObject,
            labelGroupList = productCardUiModel.labelGroupList.map {
                mapToProductCardLabelGroup(it)
            },
            hasThreeDots = hasThreeDots,
            hasAddToCartButton = isHasAddToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            stockBarLabel = productCardUiModel.stockLabel,
            stockBarPercentage = productCardUiModel.stockSoldPercentage,
        )
    }
}