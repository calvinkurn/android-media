package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionItem(
        @SerializedName("template")
        @Expose
        val template: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

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

        @SerializedName("shortcut_image")
        @Expose
        val shortcutImage: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("label")
        @Expose
        val label: String = "",

        @SerializedName("label_type")
        @Expose
        val labelType: String = "",

        @SerializedName("url_tracker")
        @Expose
        val urlTracker: String = "",

        @SerializedName("tracking")
        @Expose
        val tracking: SuggestionTracking = SuggestionTracking(),

        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: String = "",

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: Int = 0,

        @SerializedName("component_id")
        @Expose
        val componentId: String = "",

        @SerializedName("child_items")
        @Expose
        val suggestionChildItems: List<SuggestionChildItem> = listOf()
)