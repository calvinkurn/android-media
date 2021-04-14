package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionItem(
        @SerializedName("template")
        val template: String = "",

        @SerializedName("type")
        val type: String = "",

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

        @SerializedName("shortcut_url")
        val shortcutUrl: String = "",

        @SerializedName("shortcut_image")
        val shortcutImage: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("label")
        val label: String = "",

        @SerializedName("label_type")
        val labelType: String = "",

        @SerializedName("url_tracker")
        val urlTracker: String = "",

        @SerializedName("tracking")
        val tracking: SuggestionTracking = SuggestionTracking(),

        @SerializedName("discount_percentage")
        val discountPercentage: String = "",

        @SerializedName("original_price")
        val originalPrice: String = ""
)