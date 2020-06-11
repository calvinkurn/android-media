package com.tokopedia.seller.search.common.data

import com.google.gson.annotations.SerializedName

data class SellerSearchResponse(
        @SerializedName("sellerSearch")
        val sellerSearch: SellerSearch = SellerSearch()
) {
    data class SellerSearch(
            @SerializedName("data")
            val `data`: SellerSearchData = SellerSearchData()
    ) {
        data class SellerSearchData(
                @SerializedName("sections")
                val sections: List<Section> = listOf(),
                @SerializedName("count")
                val count: Int? = 0,
                @SerializedName("filters")
                val filters: List<String> = listOf()
        ) {
            data class Section(
                    @SerializedName("action_link")
                    val action_link: String? = "",
                    @SerializedName("action_title")
                    val action_title: String? = "",
                    @SerializedName("has_more")
                    val has_more: Boolean? = false,
                    @SerializedName("id")
                    val id: String? = "",
                    @SerializedName("items")
                    val items: List<Item> = listOf(),
                    @SerializedName("title")
                    val title: String? = ""
            ) {
                data class Item(
                        @SerializedName("app_url")
                        val app_url: String? = "",
                        @SerializedName("click_event")
                        val click_event: String? = "",
                        @SerializedName("description")
                        val description: String? = "",
                        @SerializedName("id")
                        val id: String? = "",
                        @SerializedName("image_url")
                        val image_url: String? = "",
                        @SerializedName("label")
                        val label: String? = "",
                        @SerializedName("ref_id")
                        val ref_id: String? = "",
                        @SerializedName("title")
                        val title: String? = "",
                        @SerializedName("url")
                        val url: String? = ""
                )
            }
        }
    }
}