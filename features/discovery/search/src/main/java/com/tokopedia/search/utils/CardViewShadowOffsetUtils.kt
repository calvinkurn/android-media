package com.tokopedia.search.utils

import androidx.cardview.widget.CardView
import com.tokopedia.productcard.ProductCardGridView
import kotlin.math.cos
import kotlin.math.roundToInt

/**
 * shadow calculation formula (include the const value) based on CardView shadow formula
 * https://developer.android.com/reference/kotlin/androidx/cardview/widget/CardView
 */
internal const val VERTICAL_SHADOW_MULTIPLIER = 1.5
internal const val BASE_RADIUS_AREA = 1
internal const val CORNER_RADIUS_DEGREE = 45.0

internal fun CardView.getVerticalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation * VERTICAL_SHADOW_MULTIPLIER + (BASE_RADIUS_AREA - cos(CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt()
}

internal fun CardView.getHorizontalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation + (BASE_RADIUS_AREA - cos(CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt()
}

internal fun ProductCardGridView.getVerticalShadowOffset(): Int {
    val maxElevation = this.getCardMaxElevation()
    val radius = this.getCardRadius()

    return (maxElevation * VERTICAL_SHADOW_MULTIPLIER + (BASE_RADIUS_AREA - cos(CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt()
}

internal fun ProductCardGridView.getHorizontalShadowOffset(): Int {
    val maxElevation = this.getCardMaxElevation()
    val radius = this.getCardRadius()

    return (maxElevation + (BASE_RADIUS_AREA - cos(CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt()
}