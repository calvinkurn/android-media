package com.tokopedia.seller.search.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerSearchResponse(
        @Expose
        @SerializedName("sellerSearch")
        val sellerSearch: SellerSearch = SellerSearch()
) {
    data class SellerSearch(
            @Expose
            @SerializedName("data")
            val `data`: SellerSearchData = SellerSearchData()
    ) {
        data class SellerSearchData(
                @Expose
                @SerializedName("sections")
                val sections: List<Section> = listOf(),
                @Expose
                @SerializedName("count")
                val count: Int? = 0,
                @Expose
                @SerializedName("filters")
                val filters: List<String> = listOf()
        ) {
            data class Section(
                    @Expose
                    @SerializedName("action_title")
                    val action_title: String? = "",
                    @Expose
                    @SerializedName("app_action_link")
                    val app_action_link: String? = "",
                    @Expose
                    @SerializedName("has_more")
                    val has_more: Boolean? = false,
                    @Expose
                    @SerializedName("id")
                    val id: String? = "",
                    @Expose
                    @SerializedName("items")
                    val items: List<Item> = listOf(),
                    @Expose
                    @SerializedName("title")
                    val title: String? = ""
            ) {
                data class Item(
                        @Expose
                        @SerializedName("app_url")
                        val app_url: String? = "",
                        @Expose
                        @SerializedName("description")
                        val description: String? = "",
                        @Expose
                        @SerializedName("id")
                        val id: String? = "",
                        @Expose
                        @SerializedName("image_url")
                        val image_url: String? = "",
                        @Expose
                        @SerializedName("label")
                        val label: String? = "",
                        @Expose
                        @SerializedName("title")
                        val title: String? = "",
                        @Expose
                        @SerializedName("url")
                        val url: String? = ""
                )
            }
        }
    }
}