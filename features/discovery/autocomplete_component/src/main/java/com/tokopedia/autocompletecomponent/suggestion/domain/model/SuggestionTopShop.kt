package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionTopShop(
        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("icon_title")
        @Expose
        val iconTitle: String = "",

        @SerializedName("icon_subtitle")
        @Expose
        val iconSubtitle: String = "",

        @SerializedName("url_tracker")
        @Expose
        val urlTracker: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("products")
        @Expose
        val topShopProducts: List<SuggestionTopShopProduct> = listOf()
)