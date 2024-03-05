package com.tokopedia.shop.common.util.productcard

import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.shop.common.util.productcard.festivity.FestivityLightProductCardColor
import com.tokopedia.shop.common.util.productcard.festivity.FestivityTransparentProductCardColor
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.productcard.reimagine.DarkThemedShopProductCard
import com.tokopedia.shop.common.util.productcard.reimagine.LightThemedShopProductCard

class ShopProductCardColorHelper {

    private val defaultProductCardColor = null

    private val transparentProductCard = object : ProductCardColor {
        override val cardBackgroundColor: Int? = android.R.color.transparent
        override val productNameTextColor: Int? = defaultProductCardColor
        override val priceTextColor: Int? = defaultProductCardColor
        override val slashPriceTextColor: Int? = defaultProductCardColor
        override val soldCountTextColor: Int? = defaultProductCardColor
        override val discountTextColor: Int? = defaultProductCardColor
        override val ratingTextColor: Int? = defaultProductCardColor
        override val buttonColorMode: ColorMode? = defaultProductCardColor
        override val labelBenefitViewColor = defaultProductCardColor
        override val shopBadgeTextColor: Int? = defaultProductCardColor
        override val quantityEditorColor = defaultProductCardColor
        override val stockBarColor = defaultProductCardColor
    }


    fun determineProductCardColorMode(
        isFestivity: Boolean,
        shouldOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String,
        makeProductCardTransparent: Boolean = true
    ): ProductCardColor {
        return when {
            isFestivity -> overrideToFestivityColor(makeProductCardTransparent, backgroundColor)
            shouldOverrideTheme -> overrideToShopThemeColor(makeProductCardTransparent, patternColorType, backgroundColor)
            else -> followDeviceThemeColor()
        }
    }

    private fun overrideToFestivityColor(
        makeProductCardTransparent: Boolean,
        shopPageBackgroundColor: String,
    ): ProductCardColor {
        return if (makeProductCardTransparent) {
            FestivityTransparentProductCardColor(labelBenefitCutoutFillColor = shopPageBackgroundColor)
        } else {
            FestivityLightProductCardColor(labelBenefitCutoutFillColor = shopPageBackgroundColor)
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
        val cardBackgroundColorResId = if (makeProductCardTransparent) {
            android.R.color.transparent
        } else {
            R.color.dms_static_white
        }

        return if (isLightThemedShop) {
            LightThemedShopProductCard(
                cardBackgroundColorResId = cardBackgroundColorResId,
                labelBenefitCutoutFillColor = backgroundColor
            )
        } else {
            DarkThemedShopProductCard(
                cardBackgroundColorResId = cardBackgroundColorResId,
                labelBenefitCutoutFillColor = backgroundColor
            )
        }
    }

    private fun followDeviceThemeColor(): ProductCardColor {
        return transparentProductCard
    }
}
