package com.tokopedia.purchase_platform.features.cart.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 20/02/18.
 */

class UpdateCartRequest {
    @SerializedName("cart_id")
    @Expose
    var cartId: Int = 0
    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0
    @SerializedName("notes")
    @Expose
    var notes: String? = null
}
