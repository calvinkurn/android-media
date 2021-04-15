package com.tokopedia.autocomplete.initialstate

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
        val originalPrice: String = ""
) {
    fun hasSlashedPrice(): Boolean {
        return discountPercentage.isNotEmpty() && originalPrice.isNotEmpty()
    }
}
