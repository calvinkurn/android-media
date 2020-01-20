package com.tokopedia.createpost.data.pojo.productsuggestion.shop


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedContentTagItems(
    @SerializedName("error")
    @Expose
    val error: String = "",

    @SerializedName("tag_items")
    @Expose
    val tagItems: List<ShopProductItem> = listOf()
)