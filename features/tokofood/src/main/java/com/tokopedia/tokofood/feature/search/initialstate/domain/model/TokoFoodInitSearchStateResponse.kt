package com.tokopedia.tokofood.feature.search.initialstate.domain.model


import com.google.gson.annotations.SerializedName

data class TokoFoodInitSearchStateResponse(
    @SerializedName("tokofoodInitSearchState")
    val tokofoodInitSearchState: TokofoodInitSearchState = TokofoodInitSearchState()
) {
    data class TokofoodInitSearchState(
        @SerializedName("sections")
        val sections: List<Section> = listOf()
    ) {
        data class Section(
            @SerializedName("header")
            val header: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("items")
            val items: List<Item> = listOf(),
            @SerializedName("labelAction")
            val labelAction: String = "",
            @SerializedName("labelText")
            val labelText: String = "",
            @SerializedName("type")
            val type: String = ""
        ) {
            data class Item(
                @SerializedName("applink")
                val applink: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("imageURL")
                val imageUrl: String = "",
                @SerializedName("label")
                val label: String = "",
                @SerializedName("labelType")
                val labelType: String = "",
                @SerializedName("shortcutAction")
                val shortcutAction: String = "",
                @SerializedName("shortcutImage")
                val shortcutImage: String = "",
                @SerializedName("subtitle")
                val subtitle: String = "",
                @SerializedName("template")
                val template: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("url")
                val url: String = ""
            )
        }
    }
}