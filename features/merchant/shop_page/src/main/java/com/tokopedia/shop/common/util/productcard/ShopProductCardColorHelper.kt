package com.tokopedia.shop.common.util.productcard

import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel

class ShopProductCardColorHelper {

    fun determineProductCardColorMode(
        isDeviceOnDarkModeTheme: Boolean,
        shouldOverrideTheme: Boolean,
        patternColorType: String,
        backgroundColor: String
    ): ProductCardColor {
        val isShopReimagine = shouldOverrideTheme
        
        return if (isShopReimagine) {
            determineColorModeForShopReimagine(patternColorType, backgroundColor)
        } else {
            //Festivity and non-shop reimagine
            determineColorModeForNonShopReimagine(isDeviceOnDarkModeTheme, backgroundColor)
        }
    }

    /**
     * If shop is reimagine, we will ignore the device selected theme and override the product card color 
     * based on the shop selected theme from `patternColorType` property instead.
     */
    private fun determineColorModeForShopReimagine(
        patternColorType: String,
        backgroundColor: String
    ): ProductCardColor {
        val isLightThemedShop = patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
        
        return if (isLightThemedShop) {
            LightThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        } else {
            DarkThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        }
    }

    /**
     * If shop is non reimagine (e.g normal shop or festivity) we still need to return dark themed and light themed product card color concrete implementation 
     * that respect the device-selected theme and make the product card background transparent
     */
    private fun determineColorModeForNonShopReimagine(
        isDeviceOnDarkModeTheme: Boolean,
        backgroundColor: String
    ): ProductCardColor {
        return if (isDeviceOnDarkModeTheme) {
            DarkThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        } else {
            LightThemedShopProductCard(labelBenefitCutoutFillColor = backgroundColor)
        }
    }
}
