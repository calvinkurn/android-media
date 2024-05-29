package com.tokopedia.shop.common.util.productcard.reimagine

import com.tokopedia.shop.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.unifyprinciples.ColorMode

data class TransparentBackgroundWithLightTextProductCard(
    val labelBenefitCutoutFillColor: String
) : ProductCardColor {
    override val cardBackgroundColor: Int = android.R.color.transparent
    override val productNameTextColor: Int = R.color.dms_static_dark_NN950_96
    override val priceTextColor: Int = R.color.dms_static_dark_NN950_96
    override val slashPriceTextColor: Int = R.color.dms_static_dark_NN950_96
    override val soldCountTextColor: Int =  R.color.dms_static_dark_NN950_96
    override val discountTextColor: Int= R.color.dms_static_dark_RN500
    override val ratingTextColor: Int = R.color.dms_static_dark_NN950_96
    override val buttonColorMode: ColorMode = ColorMode.LIGHT_MODE
    override val labelBenefitViewColor = ProductCardColor.LabelBenefitViewColor(cutoutFillColor = labelBenefitCutoutFillColor)
    override val shopBadgeTextColor: Int = R.color.dms_static_dark_NN950_96
    override val quantityEditorColor = ProductCardColor.QuantityEditorColor(
        buttonDeleteCartColorLight = R.color.dms_static_dark_NN900,
        buttonDeleteCartColorDark = R.color.dms_static_dark_NN900,
        quantityTextColor = R.color.dms_static_dark_NN950
    )
    override val stockBarColor = ProductCardColor.StockBarColor(
        backgroundColor = R.color.dms_static_dark_N100,
        stockTextColor = R.color.dms_static_dark_NN600,
        progressBarColorIsNearlyOutOfStock = R.color.dms_static_dark_RN500,
        progressBarColorIsInDemand = R.color.dms_static_dark_YN500,
        progressBarColorIsAvailable = R.color.dms_static_dark_YN300,
        progressBarTrackColor = R.color.dms_static_dark_N300
    )
    override val showOutlineView: Boolean
        get() = false

    override val ratingDotColor: Int = R.color.dms_static_dark_NN950_96
}
