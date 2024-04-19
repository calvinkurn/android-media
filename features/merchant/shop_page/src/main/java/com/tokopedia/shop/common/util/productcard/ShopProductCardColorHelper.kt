package com.tokopedia.shop.common.util.productcard

import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.shop.common.util.productcard.reimagine.TransparentBackgroundWithDarkTextProductCard
import com.tokopedia.shop.common.util.productcard.reimagine.TransparentBackgroundWithLightTextProductCard
import com.tokopedia.shop.common.util.productcard.reimagine.WhiteBackgroundProductCard
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.unifyprinciples.ColorMode

class ShopProductCardColorHelper {

    fun determineProductCardColorMode(
        isFestivity: Boolean,
        shouldOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        makeProductCardTransparent: Boolean
    ): ProductCardColor {
        return when {
            isFestivity -> overrideToFestivityColor(makeProductCardTransparent, backgroundColor)
            shouldOverrideTheme -> overrideToShopThemeColor(makeProductCardTransparent, patternColorType, backgroundColor)
            else -> followDeviceThemeColor()
        }
    }

    private fun overrideToFestivityColor(
        makeProductCardTransparent: Boolean,
        shopPageBackgroundColor: String
    ): ProductCardColor {
        return if (makeProductCardTransparent) {
            TransparentBackgroundWithLightTextProductCard(labelBenefitCutoutFillColor = shopPageBackgroundColor)
        } else {
            WhiteBackgroundProductCard(shopPageBackgroundColor)
        }
    }

    /**
     * If shop is reimagine, we will ignore the device selected theme and override the product card color
     * based on the shop selected theme from `patternColorType` property instead.
     */
    private fun overrideToShopThemeColor(
        makeProductCardTransparent: Boolean,
        patternColorType: String,
        backgroundColor: String
    ): ProductCardColor {
        val isLightThemedShop = patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
        return when {
            !makeProductCardTransparent -> WhiteBackgroundProductCard(labelBenefitCutoutFillColor = backgroundColor)
            isLightThemedShop -> TransparentBackgroundWithDarkTextProductCard(labelBenefitCutoutFillColor = backgroundColor) // Force light mode
            else -> TransparentBackgroundWithLightTextProductCard(labelBenefitCutoutFillColor = backgroundColor) // Force dark mode
        }
    }

    private fun followDeviceThemeColor(): ProductCardColor {
        val defaultProductCardColor = object : ProductCardColor {
            override val cardBackgroundColor: Int? = null
            override val productNameTextColor: Int? = null
            override val priceTextColor: Int? = null
            override val slashPriceTextColor: Int? = null
            override val soldCountTextColor: Int? = null
            override val discountTextColor: Int? = null
            override val ratingTextColor: Int? = null
            override val buttonColorMode: ColorMode? = null
            override val labelBenefitViewColor = null
            override val shopBadgeTextColor: Int? = null
            override val quantityEditorColor = null
            override val stockBarColor = null
            override val showOutlineView = true
        }

        return defaultProductCardColor
    }
}
