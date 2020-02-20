package com.tokopedia.autocomplete.initialstate

import com.google.gson.annotations.SerializedName

data class InitialStateItem(
        @SerializedName("template")
        val template: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",

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

        @SerializedName("label")
        val label: String = "",

        @SerializedName("label_type")
        val labelType: String = "",

        @SerializedName("shortcut_image")
        val shortcutImage: String = "",

        @SerializedName("id")
        val itemId: String = ""
)

