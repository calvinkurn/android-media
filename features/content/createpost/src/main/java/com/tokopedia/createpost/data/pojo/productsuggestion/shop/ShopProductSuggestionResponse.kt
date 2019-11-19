package com.tokopedia.createpost.data.pojo.productsuggestion.shop


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopProductSuggestionResponse(
    @SerializedName("feedContentTagItems")
    @Expose
    val feedContentTagItems: FeedContentTagItems = FeedContentTagItems()
)