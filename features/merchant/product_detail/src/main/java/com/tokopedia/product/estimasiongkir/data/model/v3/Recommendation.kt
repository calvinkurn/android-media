package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Recommendation(
        @SerializedName("service_name")
        @Expose
        val name: String = "",

        @SerializedName("shipping_id")
        @Expose
        val shippingId: Int = 0,

        @SerializedName("shipping_product_id")
        @Expose
        val shippingProductId: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Price = Price(),

        @SerializedName("etd")
        @Expose
        val etd: ETD = ETD(),

        @SerializedName("texts")
        @Expose
        val texts: Texts = Texts(),

        @SerializedName("insurance")
        @Expose
        val insurance: Insurance = Insurance(),

        @SerializedName("error")
        @Expose
        val error: Error = Error()
)