package com.tokopedia.minicart.common.data.request.deletecart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RemoveCartRequest(
    @SerializedName("cart_ids")
    @Expose
    var cartIds: List<String> = emptyList(),
    @SerializedName("add_wishlist")
    @Expose
    var addWishlist: Int = 0
)
