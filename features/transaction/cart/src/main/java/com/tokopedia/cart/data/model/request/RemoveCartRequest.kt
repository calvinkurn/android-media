package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 20/02/18.
 */

data class RemoveCartRequest(
    @SerializedName("cart_ids")
    @Expose
    var cartIds: List<String>? = null,
    @SerializedName("add_wishlist")
    @Expose
    var addWishlist: Int = 0
)
