package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 20/02/18.
 */

data class UpdateCartRequest(
    @SerializedName("cart_id")
    @Expose
    var cartId: String = "",
    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0,
    @SerializedName("notes")
    @Expose
    var notes: String? = null
)
