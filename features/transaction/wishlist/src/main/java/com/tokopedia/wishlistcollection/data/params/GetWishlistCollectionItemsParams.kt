package com.tokopedia.wishlistcollection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetWishlistCollectionItemsParams(
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
        var source: String = "",

        @SerializedName("chosen_address")
        var wishlistChosenAddress: WishlistChosenAddress = WishlistChosenAddress(),

        @SerializedName("in_collection")
        var inCollection: String = "",

        @SerializedName("collection_id")
        var collectionId: String = ""
): GqlParam {
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
