package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer

data class BroadMatchItemViewModel(
        val id: String = "",
        val name: String = "",
        val price: Int = 0,
        val imageUrl: String = "",
        val rating: Int = 0,
        val countReview: Int = 0,
        val url: String = "",
        val applink: String = "",
        val priceString: String = "",
        val shopLocation: String = "",
        val badgeItemViewModelList: List<BadgeItemViewModel> = listOf(),
        val freeOngkirViewModel: FreeOngkirViewModel = FreeOngkirViewModel(),
        var isWishlisted: Boolean = false,
        val position: Int = 0,
        val alternativeKeyword: String = ""
) {

    fun asImpressionObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", "/search - broad match",
                "position", position
        )
    }

    fun asClickObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", "/search - broad match",
                "position", position,
                "attribution", "none / other"
        )
    }
}