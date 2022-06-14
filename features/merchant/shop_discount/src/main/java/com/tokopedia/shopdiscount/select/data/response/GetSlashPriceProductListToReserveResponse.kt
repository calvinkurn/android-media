package com.tokopedia.shopdiscount.select.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSlashPriceProductListToReserveResponse(
    @SerializedName("getSlashPriceProductListToReserve")
    val getSlashPriceProductListToReserve: GetSlashPriceProductListToReserve = GetSlashPriceProductListToReserve()
) {
    data class GetSlashPriceProductListToReserve(
        @SerializedName("product_list")
        val productList: List<Product> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class Product(
            @SerializedName("count_variant")
            val countVariant: Int = 0,
            @SerializedName("disabled")
            val disabled: Boolean = false,
            @SerializedName("disabled_reason")
            val disabledReason: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("picture")
            val picture: String = "",
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            val price: Price = Price(),
            @SerializedName("product_id")
            val productId: String = "",
            @SerializedName("sku")
            val sku: String = "",
            @SerializedName("stock")
            val stock: String = "",
            @SerializedName("url")
            val url: String = ""
        ) {
            data class Price(
                @SerializedName("max")
                val max: Long = 0,
                @SerializedName("max_formatted")
                val maxFormatted: String = "",
                @SerializedName("min")
                val min: Long = 0,
                @SerializedName("min_formatted")
                val minFormatted: String = ""
            )
        }

        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("error_message")
            val errorMessage: List<Any> = listOf(),
            @SerializedName("process_time")
            val processTime: Double = 0.0,
            @SerializedName("reason")
            val reason: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}