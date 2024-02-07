package com.tokopedia.shop.common.util

import android.view.View
import com.tokopedia.productcard.experiments.ColorMode
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.productcard.R as productcardR

class ShopProductCardColorHelper {

    fun determineProductCardColorMode(shouldOverrideTheme: Boolean, patternColorType: String): ColorMode? {
        if (!shouldOverrideTheme) return null
        if (patternColorType.isEmpty()) return null

        val isLightThemedShop = patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
        return if (isLightThemedShop) ColorMode.LIGHT else ColorMode.DARK
    }

    fun shouldOverrideProductCardColor(shouldOverrideTheme: Boolean, patternType: String): Boolean {
        if (!shouldOverrideTheme) return false
        if (patternType.isEmpty()) return false

        return shouldOverrideTheme && patternType == ShopPageHeaderLayoutUiModel.ColorType.DARK.value
    }

    fun overrideProductCardContentToLightColor(view: View?) {
        if (view == null) return

        val buttonAddToCart = view.findViewById<UnifyButton?>(productcardR.id.productCardAddToCart)
        buttonAddToCart?.applyColorMode(com.tokopedia.unifyprinciples.ColorMode.LIGHT_MODE)
    }
}
