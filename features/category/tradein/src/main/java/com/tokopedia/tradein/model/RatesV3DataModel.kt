package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class RatesV3DataModel(
    @SerializedName("ratesV3Api")
    val ratesV3Api: RatesV3Api
) {
    data class RatesV3Api(
        @SerializedName("ratesv3")
        val ratesv3: Ratesv3
    ) {
        data class Ratesv3(
            @SerializedName("id")
            val id: String,
            @SerializedName("rates_id")
            val ratesId: String,
            @SerializedName("services")
            val services: List<Service>,
            @SerializedName("type")
            val type: String
        ) {
            data class Service(
                @SerializedName("products")
                val products: List<Product>,
                @SerializedName("status")
                val status: Int
            ) {
                data class Product(
                    @SerializedName("price")
                    val price: Price?
                ) {
                    data class Price(
                        @SerializedName("formatted_price")
                        val formattedPrice: String,
                        @SerializedName("price")
                        val price: Int
                    )
                }
            }
        }
    }
}