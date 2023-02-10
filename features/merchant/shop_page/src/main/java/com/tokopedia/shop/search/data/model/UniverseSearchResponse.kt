package com.tokopedia.shop.search.data.model

import com.google.gson.annotations.SerializedName

data class UniverseSearchResponse(
    @SerializedName("universe_search")
    var universeSearch: UniverseSearch = UniverseSearch()
) {
    data class UniverseSearch(
        @SerializedName("data")
        var data: MutableList<Data> = mutableListOf()
    ) {
        data class Data(
            @SerializedName("id")
            var id: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("items")
            var items: MutableList<Item> = mutableListOf()

        ) {
            data class Item(
                @SerializedName("location")
                var location: String = "",
                @SerializedName("imageURI")
                var imageUri: String = "",
                @SerializedName("applink")
                var appLink: String = "",
                @SerializedName("url")
                var url: String = "",
                @SerializedName("keyword")
                var keyword: String = "",
                @SerializedName("recom")
                var recom: String = "",
                @SerializedName("sc")
                var sc: Int = 0,
                @SerializedName("isOfficial")
                var isOfficial: Boolean = false,
                @SerializedName("post_count")
                var postCount: Int = 0,
                @SerializedName("affiliate_username")
                var affiliateUsername: String = ""
            )
        }
    }
}
