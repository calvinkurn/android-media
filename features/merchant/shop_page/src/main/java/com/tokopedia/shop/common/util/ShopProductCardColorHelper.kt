package com.tokopedia.shop.common.util

import com.tokopedia.productcard.experiments.ColorMode
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel

class ShopProductCardColorHelper {

    fun determineProductCardColorMode(shouldOverrideTheme: Boolean, patternColorType: String): ColorMode? {
        if (!shouldOverrideTheme) return null
        if (patternColorType.isEmpty()) return null
        
        val isLightThemedShop = patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
        return if (isLightThemedShop) ColorMode.LIGHT else ColorMode.DARK
    }

}
