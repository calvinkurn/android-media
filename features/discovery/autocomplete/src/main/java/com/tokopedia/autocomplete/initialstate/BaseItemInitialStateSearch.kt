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
        val productId: String = ""
) {
    fun getObjectDataLayerForRecentView(position: Int): Any {
        return DataLayer.mapOf(
                "name", getName(),
                "id", productId,
                "price", "",
                "brand", "none",
                "category", "none / other",
                "variant", "none",
                "list", "/search - recentview - product",
                "position", position
        )
    }

    fun getObjectDataLayerForPromo(position: Int): Any {
        return DataLayer.mapOf(
                "id", productId,
                "name", "/search - initial state",
                "creative", title,
                "position", position
        )
    }

    private fun getName() : String {
        return if (title.isEmpty()) "none / other"
        else title
    }
}
