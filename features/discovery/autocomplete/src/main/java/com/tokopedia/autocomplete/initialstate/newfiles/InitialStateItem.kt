package com.tokopedia.autocomplete.initialstate.newfiles

import com.google.gson.annotations.SerializedName

data class InitialStateItem(
        @SerializedName("template")
        var template: String = "",

        @SerializedName("image_url")
        var imageUrl: String = "",

        @SerializedName("applink")
        var applink: String = "",

        @SerializedName("url")
        var url: String = "",

        @SerializedName("title")
        var title: String = "",

        @SerializedName("subtitle")
        var subtitle: String = "",

        @SerializedName("icon_title")
        var iconTitle: String = "",

        @SerializedName("icon_subtitle")
        var iconSubtitle: String = "",

        @SerializedName("label")
        var label: String = "",

        @SerializedName("shortcut_url")
        var shortcutUrl: String = "",

        @SerializedName("shortcut_image")
        var shortcutImage: String = "",

        @SerializedName("id")
        var itemId: String = "",

        @SerializedName("price")
        var price: String = ""
)

