package com.tokopedia.autocomplete.util

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
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

internal fun Map<String, Any>?.getValueString(key: String): String {
    this ?: return ""

    return get(key)?.toString() ?: ""
}

internal fun RecyclerView.addItemDecorationIfNotExists(itemDecoration: RecyclerView.ItemDecoration) {
    val hasNoItemDecoration = itemDecorationCount == 0
    if (hasNoItemDecoration) addItemDecoration(itemDecoration)
}