package com.tokopedia.checkout.data.model.request.changeaddress

import com.google.gson.annotations.SerializedName

data class DataChangeAddressRequest(
    @SerializedName("cart_id_str")
    var cartIdStr: String? = null,
    @SerializedName("product_id")
    var productId: Long = 0,
    @SerializedName("address_id")
    var addressId: String? = null,
    @SerializedName("note")
    var notes: String? = null,
    @SerializedName("qty")
    var quantity: Int = 0,
    @SerializedName("is_indomaret")
    var isIndomaret: Boolean = false
)