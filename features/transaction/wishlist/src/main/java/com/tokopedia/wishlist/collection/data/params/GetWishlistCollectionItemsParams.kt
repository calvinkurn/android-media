package com.tokopedia.wishlist.collection.data.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.localizationchooseaddress.common.ChosenAddress

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
    var wishlistChosenAddress: ChosenAddress = ChosenAddress(),

    @SerializedName("in_collection")
    var inCollection: String = "",

    @SerializedName("collection_id")
    var collectionId: String = ""
) : GqlParam {
    data class WishlistSortFilterParam(
        @SerializedName("name")
        var name: String = "",

        @SerializedName("selected")
        var selected: ArrayList<String> = arrayListOf()
    )
}
