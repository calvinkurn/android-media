package com.tokopedia.shop.common.util.productcard.reimagine

import com.tokopedia.shop.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.unifyprinciples.ColorMode

data class TransparentBackgroundWithDarkTextProductCard(
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
    override val labelBenefitViewColor = ProductCardColor.LabelBenefitViewColor(cutoutFillColor = labelBenefitCutoutFillColor)
    override val shopBadgeTextColor: Int = R.color.dms_static_Unify_NN600_light
    override val quantityEditorColor = ProductCardColor.QuantityEditorColor(
        buttonDeleteCartColorLight = R.color.dms_static_light_NN900,
        buttonDeleteCartColorDark = R.color.dms_static_light_NN900,
        quantityTextColor = R.color.dms_static_light_NN950
    )
    override val stockBarColor = ProductCardColor.StockBarColor(
        backgroundColor = R.color.dms_static_light_N50,
        stockTextColor = R.color.dms_static_light_NN600,
        progressBarColorIsNearlyOutOfStock = R.color.dms_static_light_RN500,
        progressBarColorIsInDemand = R.color.dms_static_light_YN500,
        progressBarColorIsAvailable = R.color.dms_static_light_YN300,
        progressBarTrackColor = R.color.dms_static_light_N100
    )
    override val showOutlineView: Boolean
        get() = false

    override val ratingDotColor: Int = R.color.dms_static_light_NN400
}
