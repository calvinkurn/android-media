package com.tokopedia.productcard.experiments

import com.tokopedia.unifyprinciples.ColorMode

interface ProductCardColor {
    val cardBackgroundColor: Int?
    val productNameTextColor: Int?
    val priceTextColor: Int?
    val slashPriceTextColor: Int?
    val soldCountTextColor: Int?
    val discountTextColor: Int?
    val ratingTextColor: Int?
    val shopBadgeTextColor: Int?
    val buttonColorMode: ColorMode?
    val labelBenefitViewColor: LabelBenefitViewColor?
    val quantityEditorColor: QuantityEditorColor?
    val stockBarColor: StockBarColor?
    val showOutlineView: Boolean
    val ratingDotColor: Int?

    data class LabelBenefitViewColor(val cutoutFillColor: String)
    data class QuantityEditorColor(
        val buttonDeleteCartColorLight: Int?,
        val buttonDeleteCartColorDark: Int?,
        val quantityTextColor: Int?
    )

    data class StockBarColor(
        val backgroundColor: Int?,
        val stockTextColor: Int?,
        val progressBarColorIsInDemand: Int?,
        val progressBarColorIsNearlyOutOfStock: Int?,
        val progressBarColorIsAvailable: Int?,
        val progressBarTrackColor: Int?
    )
}
