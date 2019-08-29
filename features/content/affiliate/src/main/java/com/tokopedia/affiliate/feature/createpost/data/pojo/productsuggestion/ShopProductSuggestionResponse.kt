package com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductSuggestionResponse(
    @SerializedName("feedContentTagItems")
    @Expose
    val feedContentTagItems: FeedContentTagItems = FeedContentTagItems()
)