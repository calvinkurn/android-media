package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionTopShop(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("id")
        val id: String = "",

        @SerializedName("applink")
        val applink: String = "",

        @SerializedName("url")
        val url: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("subtitle")
        val subtitle: String = "",

        @SerializedName("icon_title")
        val iconTitle: String = "",

        @SerializedName("icon_subtitle")
        val iconSubtitle: String = "",

        @SerializedName("url_tracker")
        val urlTracker: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("products")
        val topShopProducts: List<SuggestionTopShopProduct> = listOf()
)