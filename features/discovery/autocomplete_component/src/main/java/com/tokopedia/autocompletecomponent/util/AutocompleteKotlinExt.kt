package com.tokopedia.autocompletecomponent.util

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.cos
import kotlin.math.roundToInt

@Suppress("MagicNumber")
internal fun CardView.getVerticalShadowOffset(): Int {
    val maxElevation = this.maxCardElevation
    val radius = this.radius

    return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt()
}

@Suppress("MagicNumber")
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

internal fun Map<String, String>.getWithDefault(key: String, defaultValue: String): String {
    val currentValue = this[key] ?: ""

    return if (currentValue.isEmpty()) defaultValue else currentValue
}

internal fun MutableMap<String, String>.removeKeys(vararg keys: String) {
    keys.forEach { this.remove(it) }
}