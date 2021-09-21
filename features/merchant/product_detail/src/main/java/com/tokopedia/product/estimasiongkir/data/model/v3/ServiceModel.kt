package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceModel(
        @SerializedName("service_name")
        @Expose
        val name: String = "",

        @SerializedName("service_id")
        @Expose
        val id: Int = 0,

        @SerializedName("service_order")
        @Expose
        val order: Int = 0,

        @SerializedName("status")
        @Expose
        val status: Int = 0,

        @SerializedName("range_price")
        @Expose
        val rangePrice: RangePrice = RangePrice(),

        @SerializedName("etd")
        @Expose
        val etd: ETD = ETD(),

        @SerializedName("texts")
        @Expose
        val texts: Texts = Texts(),

        @SerializedName("products")
        @Expose
        val products: List<ServiceProduct> = listOf(),

        @SerializedName("error")
        @Expose
        val error: Error = Error(),

        @SerializedName("is_promo")
        @Expose
        val isPromo: Int = 0,

        @SerializedName("cod")
        @Expose
        val cod: COD = COD(),

        @SerializedName("order_priority")
        @Expose
        val orderPriority: OrderPriority = OrderPriority()
)