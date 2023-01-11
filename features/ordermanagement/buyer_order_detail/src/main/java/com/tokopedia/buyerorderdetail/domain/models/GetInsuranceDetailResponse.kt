package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetInsuranceDetailResponse(
    @SerializedName("data")
    @Expose
    val data: Data? = null
) {
    data class Data(
        @SerializedName("ppGetInsuranceDetail")
        @Expose
        val ppGetInsuranceDetail: PpGetInsuranceDetail? = null
    ) {
        data class PpGetInsuranceDetail(
            @SerializedName("data")
            @Expose
            val data: Data? = null
        ) {
            data class Data(
                @SerializedName("orderConfig")
                @Expose
                val orderConfig: OrderConfig? = null,
                @SerializedName("protectionProduct")
                @Expose
                val protectionProduct: ProtectionProduct? = null
            ) {
                data class OrderConfig(
                    @SerializedName("icon")
                    @Expose
                    val icon: Icon? = null,
                    @SerializedName("redirection")
                    @Expose
                    val redirection: String? = null,
                    @SerializedName("wording")
                    @Expose
                    val wording: Wording? = null
                ) {
                    data class Icon(
                        @SerializedName("banner")
                        @Expose
                        val banner: String? = null
                    )

                    data class Wording(
                        @SerializedName("id")
                        @Expose
                        val id: Id? = null
                    ) {
                        data class Id(
                            @SerializedName("bannerSubtitle")
                            @Expose
                            val bannerSubtitle: String? = null,
                            @SerializedName("bannerTitle")
                            @Expose
                            val bannerTitle: String? = null
                        )
                    }
                }

                data class ProtectionProduct(
                    @SerializedName("protections")
                    @Expose
                    val protections: List<Protection?>? = null
                ) {
                    data class Protection(
                        @SerializedName("bundleID")
                        @Expose
                        val bundleID: String? = null,
                        @SerializedName("isBundle")
                        @Expose
                        val isBundle: Boolean? = null,
                        @SerializedName("productID")
                        @Expose
                        val productID: String? = null,
                        @SerializedName("protectionConfig")
                        @Expose
                        val protectionConfig: ProtectionConfig? = null
                    ) {
                        data class ProtectionConfig(
                            @SerializedName("icon")
                            @Expose
                            val icon: Icon? = null,
                            @SerializedName("wording")
                            @Expose
                            val wording: Wording? = null
                        ) {
                            data class Icon(
                                @SerializedName("label")
                                @Expose
                                val label: String? = null
                            )

                            data class Wording(
                                @SerializedName("id")
                                @Expose
                                val id: Id? = null
                            ) {
                                data class Id(
                                    @SerializedName("label")
                                    @Expose
                                    val label: String? = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
