package com.tokopedia.wishlistcommon.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.common.ChosenAddress

data class WishlistV2Params(
    @SerializedName("page")
    var page: Int = 1,

    @SerializedName("limit")
    var limit: Int = 20,

    @SerializedName("offset")
    var offset: Int = 0,

    @SerializedName("query")
    var query: String = "",

    @SerializedName("sort_filters")
    var sortFilters: ArrayList<WishlistSortFilterParam> = arrayListOf(),

    @SerializedName("source")
    var source: String = "wishlist",

    @SerializedName("chosen_address")
    var wishlistChosenAddress: ChosenAddress = ChosenAddress()
) {
    data class WishlistSortFilterParam(
        @SerializedName("name")
        var name: String = "",

        @SerializedName("selected")
        var selected: ArrayList<String> = arrayListOf()
    )
}
