package com.tokopedia.product.detail.data.model.checkouttype

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

val EXPRESS = "express" // this is from api
val DEFAULT = "default" // this is from api

data class GetCheckoutTypeResponse(
    @SerializedName("get_cart_type")
    @Expose
    val getCartType: GetCartType = GetCartType()
)

data class GetCartType(

    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
    val isExpress: Boolean
    get() {
        return data.isExpress
    }
}

data class Data(

    @SerializedName("cart_type")
    @Expose
    val cartType: String = DEFAULT
) {
    val isExpress: Boolean
        get() {
            return EXPRESS.equals(cartType)
        }
}