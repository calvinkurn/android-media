package com.tokopedia.autocomplete.initialstate

import com.google.gson.annotations.SerializedName
import com.tokopedia.analyticconstant.DataLayer

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
        val itemId: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("discount_percentage")
        val discountPercentage: String = "",

        @SerializedName("original_price")
        val originalPrice: String = ""
) {
        fun getObjectDataLayerForRecentView(position: Int): Any {
                return DataLayer.mapOf(
                        "name", getName(),
                        "id", itemId,
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
                        "id", itemId,
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

