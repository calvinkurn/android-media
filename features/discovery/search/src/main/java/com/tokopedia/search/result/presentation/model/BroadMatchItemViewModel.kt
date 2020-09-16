package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder

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
        val alternativeKeyword: String = "",
        val isOrganicAds: Boolean = false,
        val topAdsViewUrl: String = "",
        val topAdsClickUrl: String = "",
        val topAdsWishlistUrl: String = ""
): ImpressHolder() {

    fun asImpressionObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", getDataLayerList(),
                "position", position
        )
    }

    private fun getDataLayerList(): String {
        return "/search - broad match - ${if (isOrganicAds) "organic ads" else "organic"}"
    }

    fun asClickObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", getDataLayerList(),
                "position", position,
                "attribution", "none / other"
        )
    }
}