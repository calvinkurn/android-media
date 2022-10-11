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
            val `data`: Data?
        ) {
            data class Data(
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
                        @SerializedName("productID")
                        @Expose
                        val productID: String?,
                        @SerializedName("protectionConfig")
                        @Expose
                        val protectionConfig: ProtectionConfig?
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
        }
    }
}
