package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutResultParams (
        @SerializedName("carts")
        @Expose
        val carts : Carts = Carts()
)

class Carts(
        @SerializedName("meta_data")
        @Expose
        val meta_data: String = "",
        @SerializedName("business_type")
        @Expose
        val business_type: Int = 3,
        @SerializedName("cart_info")
        @Expose
        val cart_info: Array<String> = arrayOf()
)