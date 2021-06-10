package com.tokopedia.autocomplete.initialstate

import com.tokopedia.analyticconstant.DataLayer

data class BaseItemInitialStateSearch(
        val template: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val subtitle: String = "",
        val iconTitle: String = "",
        val iconSubtitle: String = "",
        val label: String = "",
        val labelType: String = "",
        val shortcutImage: String = "",
        val productId: String = "",
        val type: String = "",
        val featureId: String = "",
        val header: String = "",
        val discountPercentage: String = "",
        val originalPrice: String = "",
        val position: Int = 0,
        val dimension90: String = ""
) {
    fun hasSlashedPrice(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty()
    }

    fun getRecentViewAsObjectDataLayer(): Any = DataLayer.mapOf(
            "name", title,
            "id", productId,
            "price", "",
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "position", position,
            "dimension90", dimension90
    )
}
