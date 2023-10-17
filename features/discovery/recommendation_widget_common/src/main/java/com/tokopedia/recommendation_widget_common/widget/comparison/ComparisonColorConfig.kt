package com.tokopedia.recommendation_widget_common.widget.comparison

/**
 * Color config model for comparison widget
 * @param textColor hex color for texts
 * @param anchorBackgroundColor hex color for anchor specs background & dividers
 * @param ctaTextColor hex color for cta text
 * @param productCardForceLightMode toggle to force product card to light mode
 */
data class ComparisonColorConfig(
    val textColor: String? = null,
    val anchorBackgroundColor: String? = null,
    val ctaTextColor: String? = null,
    val productCardForceLightMode: Boolean = false,
)
