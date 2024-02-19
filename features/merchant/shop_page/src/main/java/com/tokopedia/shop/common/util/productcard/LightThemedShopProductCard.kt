package com.tokopedia.shop.common.util.productcard

import com.tokopedia.shop.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.unifyprinciples.ColorMode

data class LightThemedShopProductCard(
    val labelBenefitCutoutFillColor: String
): ProductCardColor {
    override val cardBackgroundColor: Int = android.R.color.transparent
    override val productNameTextColor: Int = R.color.dms_static_light_NN950_96
    override val priceTextColor: Int = R.color.dms_static_light_NN950_96
    override val slashPriceTextColor: Int = R.color.dms_static_light_NN950_44
    override val soldCountTextColor: Int =  R.color.dms_static_light_NN950_68
    override val discountTextColor: Int= R.color.dms_static_light_RN500
    override val ratingTextColor: Int = R.color.dms_static_light_NN950_68
    override val buttonColorMode: ColorMode = ColorMode.LIGHT_MODE
    override val labelBenefitViewColor: ProductCardColor.LabelBenefitViewColor = ProductCardColor.LabelBenefitViewColor(cutoutFillColor = labelBenefitCutoutFillColor)
}
