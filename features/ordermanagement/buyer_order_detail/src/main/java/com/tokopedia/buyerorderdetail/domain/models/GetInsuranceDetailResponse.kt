package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetInsuranceDetailResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data?
) {
    data class Data(
        @SerializedName("ppGetInsuranceDetail")
        @Expose
        val ppGetInsuranceDetail: PpGetInsuranceDetail?
    ) {
        data class PpGetInsuranceDetail(
            @SerializedName("data")
            @Expose
            val `data`: Data?,
            @SerializedName("header")
            @Expose
            val header: Header?
        ) {
            data class Data(
                @SerializedName("insuranceOrderID")
                @Expose
                val insuranceOrderID: Int?,
                @SerializedName("insuranceOrderStatus")
                @Expose
                val insuranceOrderStatus: String?,
                @SerializedName("invoice")
                @Expose
                val invoice: String?,
                @SerializedName("orderConfig")
                @Expose
                val orderConfig: OrderConfig?,
                @SerializedName("protectionProduct")
                @Expose
                val protectionProduct: ProtectionProduct?
            ) {
                data class OrderConfig(
                    @SerializedName("icon")
                    @Expose
                    val icon: Icon?,
                    @SerializedName("redirection")
                    @Expose
                    val redirection: String?,
                    @SerializedName("wording")
                    @Expose
                    val wording: Wording?
                ) {
                    data class Icon(
                        @SerializedName("banner")
                        @Expose
                        val banner: String?
                    )

                    data class Wording(
                        @SerializedName("id")
                        @Expose
                        val id: Id?
                    ) {
                        data class Id(
                            @SerializedName("bannerSubtitle")
                            @Expose
                            val bannerSubtitle: String?,
                            @SerializedName("bannerTitle")
                            @Expose
                            val bannerTitle: String?
                        )
                    }
                }

                data class ProtectionProduct(
                    @SerializedName("protections")
                    @Expose
                    val protections: List<Protection?>?
                ) {
                    data class Protection(
                        @SerializedName("bundleID")
                        @Expose
                        val bundleID: Int?,
                        @SerializedName("isBundle")
                        @Expose
                        val isBundle: Boolean?,
                        @SerializedName("orderDetailID")
                        @Expose
                        val orderDetailID: Int?,
                        @SerializedName("productID")
                        @Expose
                        val productID: String?,
                        @SerializedName("protectionConfig")
                        @Expose
                        val protectionConfig: ProtectionConfig?,
                        @SerializedName("protectionID")
                        @Expose
                        val protectionID: Int?,
                        @SerializedName("protectionStatus")
                        @Expose
                        val protectionStatus: String?,
                        @SerializedName("protectionType")
                        @Expose
                        val protectionType: String?
                    ) {
                        data class ProtectionConfig(
                            @SerializedName("icon")
                            @Expose
                            val icon: Icon?,
                            @SerializedName("wording")
                            @Expose
                            val wording: Wording?
                        ) {
                            data class Icon(
                                @SerializedName("label")
                                @Expose
                                val label: String?
                            )

                            data class Wording(
                                @SerializedName("id")
                                @Expose
                                val id: Id?
                            ) {
                                data class Id(
                                    @SerializedName("label")
                                    @Expose
                                    val label: String?
                                )
                            }
                        }
                    }
                }
            }

            data class Header(
                @SerializedName("error_code")
                @Expose
                val errorCode: String?,
                @SerializedName("messages")
                @Expose
                val messages: List<Any?>?,
                @SerializedName("process_time")
                @Expose
                val processTime: Double?,
                @SerializedName("reason")
                @Expose
                val reason: String?
            )
        }
    }
}
