package com.tokopedia.product.detail.data.model.checkouttype

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val CART_TYPE_EXPRESS = "express" // this is from api
const val CART_TYPE_DEFAULT = "default" // this is from api
const val CART_TYPE_OCS = "ocs" // this is from api

data class GetCheckoutTypeResponse(
    @SerializedName("get_cart_type")
    @Expose
    val getCartType: GetCartType = GetCartType()
)

data class GetCartType(

    @SerializedName("data")
    @Expose
    val data: Data = Data()
)

data class Data(

    @SerializedName("cart_type")
    @Expose
    val cartType: String = CART_TYPE_DEFAULT
)