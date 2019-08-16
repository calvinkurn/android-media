package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class MoneyInCourierResponse(
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("ratesV4")
        val ratesV4: RatesV4?
    ) {
        data class RatesV4(
            @SerializedName("data")
            val `data`: Data?
        ) {
            data class Data(
                @SerializedName("error")
                val error: Error?,
                @SerializedName("id")
                val id: String?,
                @SerializedName("rates_id")
                val ratesId: String?,
                @SerializedName("services")
                val services: List<Service?>?,
                @SerializedName("type")
                val type: String?
            ) {
                data class Service(
                    @SerializedName("desc")
                    val desc: String?,
                    @SerializedName("error")
                    val error: Error?,
                    @SerializedName("etd")
                    val etd: Etd?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("price")
                    val price: Price?,
                    @SerializedName("products")
                    val products: List<Product?>?,
                    @SerializedName("status")
                    val status: Int?
                ) {
                    data class Price(
                        @SerializedName("range")
                        val range: Range?
                    ) {
                        data class Range(
                            @SerializedName("max")
                            val max: Int?,
                            @SerializedName("min")
                            val min: Int?,
                            @SerializedName("text")
                            val text: String?
                        )
                    }

                    data class Product(
                        @SerializedName("error")
                        val error: Error?,
                        @SerializedName("etd")
                        val etd: Etd?,
                        @SerializedName("features")
                        val features: Features?,
                        @SerializedName("price")
                        val price: Price?,
                        @SerializedName("shipper")
                        val shipper: Shipper?,
                        @SerializedName("shipper_product")
                        val shipperProduct: ShipperProduct?,
                        @SerializedName("status")
                        val status: Int?,
                        @SerializedName("weight")
                        val weight: Int?
                    ) {
                        data class Features(
                            @SerializedName("money_in")
                            val moneyIn: MoneyIn?
                        ) {
                            data class MoneyIn(
                                @SerializedName("shipper_name")
                                val shipperName: String?,
                                @SerializedName("text_price")
                                val textPrice: String?,
                                @SerializedName("value_price")
                                val valuePrice: Int?
                            )
                        }

                        data class Error(
                            @SerializedName("id")
                            val id: String?,
                            @SerializedName("message")
                            val message: String?
                        )

                        data class Price(
                            @SerializedName("text")
                            val text: String?,
                            @SerializedName("value")
                            val value: Int?
                        )

                        data class Shipper(
                            @SerializedName("id")
                            val id: Int?,
                            @SerializedName("name")
                            val name: String?
                        )

                        data class ShipperProduct(
                            @SerializedName("desc")
                            val desc: String?,
                            @SerializedName("id")
                            val id: Int?,
                            @SerializedName("name")
                            val name: String?
                        )

                        data class Etd(
                            @SerializedName("max")
                            val max: Int?,
                            @SerializedName("min")
                            val min: Int?,
                            @SerializedName("text")
                            val text: String?
                        )
                    }

                    data class Error(
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("message")
                        val message: String?
                    )

                    data class Etd(
                        @SerializedName("max")
                        val max: Int?,
                        @SerializedName("min")
                        val min: Int?,
                        @SerializedName("text")
                        val text: String?
                    )
                }

                data class Error(
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("message")
                    val message: String?
                )
            }
        }
    }
}