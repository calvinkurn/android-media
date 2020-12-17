package com.tokopedia.search.utils

import androidx.cardview.widget.CardView
import com.tokopedia.productcard.ProductCardGridView
import kotlin.math.cos
import kotlin.math.roundToInt

internal fun CardView.getVerticalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

internal fun CardView.getHorizontalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

internal fun ProductCardGridView.getVerticalShadowOffset(): Int {
    val maxElevation = this.getCardMaxElevation()
    val radius = this.getCardRadius()

    return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

internal fun ProductCardGridView.getHorizontalShadowOffset(): Int {
    val maxElevation = this.getCardMaxElevation()
    val radius = this.getCardRadius()

    return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}