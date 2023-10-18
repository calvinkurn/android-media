package com.tokopedia.wishlistcommon.data

import com.google.gson.annotations.SerializedName

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
    var wishlistChosenAddress: WishlistChosenAddress = WishlistChosenAddress()
) {
    data class WishlistSortFilterParam(
        @SerializedName("name")
        var name: String = "",

        @SerializedName("selected")
        var selected: ArrayList<String> = arrayListOf()
    )

    data class WishlistChosenAddress(
        @SerializedName("district_id")
        var districtId: String = "",

        @SerializedName("city_id")
        var cityId: String = "",

        @SerializedName("latitude")
        var latitude: String = "",

        @SerializedName("longitude")
        var longitude: String = "",

        @SerializedName("postal_code")
        var postalCode: String = "",

        @SerializedName("address_id")
        var addressId: String = ""
    )
}
