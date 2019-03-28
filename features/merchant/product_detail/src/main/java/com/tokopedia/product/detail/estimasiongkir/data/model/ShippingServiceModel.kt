package com.tokopedia.product.detail.estimasiongkir.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShippingServiceModel (
    @SerializedName("service_name")
    @Expose
    val name: String = "",

    @SerializedName("service_etd")
    @Expose
    val etd: String = "",

    @SerializedName("service_range_price")
    @Expose
    val rangePrice: String = "",

    @SerializedName("service_notes")
    @Expose
    val notes: String = "",

    @SerializedName("products")
    @Expose
    val products: List<Product> = listOf()){

    data class Product (
        @SerializedName("shipper_name")
        @Expose
        val name: String = "",

        @SerializedName("shipper_product_name")
        @Expose
        private val productName: String = "",

        @SerializedName("shipper_formatted_price")
        @Expose
        val fmtPrice: String = "",

        @SerializedName("shipper_etd")
        @Expose
        val etd: String = ""
    )
}
