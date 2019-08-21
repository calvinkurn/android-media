package com.tokopedia.tradein.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MoneyInCourierResponse(
    @SerializedName("data")
    val data: ResponseData?
) {
    data class ResponseData(
        @SerializedName("ratesV4")
        val ratesV4: RatesV4
    ) {
        data class RatesV4(
            @SerializedName("data")
            val data: Data
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
                data class Error(
                    @SerializedName("id")
                    val id: String?,
                    @SerializedName("message")
                    val message: String?,
                    @SerializedName("status")
                    val status: Int?
                )

                data class Service(
                    @SerializedName("error")
                    val error: Error?,
                    @SerializedName("etd")
                    val etd: Etd?,
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("index")
                    val index: Int?,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("price")
                    val price: Price?,
                    @SerializedName("products")
                    val products: List<Product?>?,
                    @SerializedName("status")
                    val status: Int?
                ) {
                    data class Product(
                        @SerializedName("error")
                        val error: Error?,
                        @SerializedName("etd")
                        val etd: Etd?,
                        @SerializedName("features")
                        val features: Features?,
                        @SerializedName("is_show_map")
                        val isShowMap: Boolean?,
                        @SerializedName("price")
                        val price: Price?,
                        @SerializedName("priority")
                        val priority: Int?,
                        @SerializedName("shipper")
                        val shipper: Shipper?,
                        @SerializedName("status")
                        val status: Int?
                    ) {
                        data class Price(
                            @SerializedName("text")
                            val text: String?,
                            @SerializedName("value")
                            val value: Int?
                        )

                        data class Features(
                            @SerializedName("money_in")
                            val moneyIn: MoneyIn?
                        ) {
                            @Parcelize
                            data class MoneyIn(
                                @SerializedName("shipper_name")
                                val shipperName: String?,
                                @SerializedName("text_price")
                                val textPrice: String?,
                                @SerializedName("value_price")
                                val valuePrice: Int?
                            ) : Parcelable
                        }

                        data class Error(
                            @SerializedName("id")
                            val id: String?,
                            @SerializedName("message")
                            val message: String?,
                            @SerializedName("status")
                            val status: Int?
                        )

                        data class Etd(
                            @SerializedName("range")
                            val range: Range?,
                            @SerializedName("text")
                            val text: String?,
                            @SerializedName("value")
                            val value: Int?
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

                        data class Shipper(
                            @SerializedName("id")
                            val id: Int?,
                            @SerializedName("name")
                            val name: String?,
                            @SerializedName("shipper_product")
                            val shipperProduct: ShipperProduct?
                        ) {
                            data class ShipperProduct(
                                @SerializedName("description")
                                val description: String?,
                                @SerializedName("id")
                                val id: Int?,
                                @SerializedName("name")
                                val name: String?
                            )
                        }
                    }

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

                    data class Etd(
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

                    data class Error(
                        @SerializedName("id")
                        val id: String?,
                        @SerializedName("message")
                        val message: String?,
                        @SerializedName("status")
                        val status: Int?
                    )
                }
            }
        }
    }
}