package com.tokopedia.wishlist.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/10/21.
 */
data class WishlistV2Params(
        @SerializedName("page")
        var page: Int = 1,

        @SerializedName("limit")
        var limit: Int = 8,

        @SerializedName("offset")
        var offset: Int = 0,

        @SerializedName("query")
        var query: String = "",

        @SerializedName("sort_filters")
        var sortFilters: List<WishlistSortFilterParam> = listOf(),

        @SerializedName("source")
        var source: String = "wishlist"
) {
        data class WishlistSortFilterParam(
                @SerializedName("name")
                var name: String = "",

                @SerializedName("selected")
                var selected: List<String> = listOf()
        )
}
