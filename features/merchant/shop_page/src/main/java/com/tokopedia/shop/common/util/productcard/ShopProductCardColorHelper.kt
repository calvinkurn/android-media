package com.tokopedia.shop.common.util.productcard

import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel

class ShopProductCardColorHelper {

    fun determineProductCardColorMode(
        shouldOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String
    ): ProductCardColor? {
        if (!shouldOverrideTheme || patternColorType.isEmpty() || backgroundColor.isEmpty()) return null

        val isLightThemedShop =
            patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
        return if (isLightThemedShop) {
            LightThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        } else {
            DarkThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        }
    }
}
