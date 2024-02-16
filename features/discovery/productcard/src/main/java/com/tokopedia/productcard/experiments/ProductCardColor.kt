package com.tokopedia.productcard.experiments

import com.tokopedia.unifyprinciples.ColorMode

interface ProductCardColor {
    val cardBackgroundColor: Int
    val productNameTextColor: Int
    val priceTextColor: Int
    val slashPriceTextColor: Int
    val soldCountTextColor: Int
    val discountTextColor: Int
    val ratingTextColor: Int
    val buttonColorMode: ColorMode
    val labelBenefitViewColor: LabelBenefitViewColor
    
    data class LabelBenefitViewColor(val cutoutFillColor: String)
}
