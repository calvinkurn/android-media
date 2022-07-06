package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import com.google.gson.annotations.SerializedName


data class WishlistParam(
    @SerializedName("page")
    var page: Int = 1,

    @SerializedName("limit")
    var limit: Int = 5,

    @SerializedName("sort_filters")
    var sortFilters: ArrayList<WishlistSortFilterParam> = arrayListOf(),

    @SerializedName("source")
    var source: String = "navigation"
) {
    data class WishlistSortFilterParam(
        @SerializedName("name")
        var name: String = "",

        @SerializedName("selected")
        var selected: ArrayList<String> = arrayListOf()
    )
}
