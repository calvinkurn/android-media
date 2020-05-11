package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class CartList(
    @SerializedName("cart_id")
    @Expose
    val cartId: Int = 0,
    @SerializedName("user_address_id")
    @Expose
    val userAddressId: Int = 0,
    @SerializedName("shop")
    @Expose
    val shop: Shop? = null,
    @SerializedName("product")
    @Expose
    val product: Product? = null,
    @SerializedName("errors")
    @Expose
    val errors: List<String> = ArrayList(),
    @SerializedName("messages")
    @Expose
    val messages: List<String> = ArrayList()
)
