package com.tokopedia.chatbot.domain.resolink


import com.google.gson.annotations.SerializedName

data class ResoLinkResponse(
    @SerializedName("get_resolution_link")
    val getResolutionLink: GetResolutionLink?
) {
    data class GetResolutionLink(
            @SerializedName("config")
        val config: String?,
            @SerializedName("data")
        val resolutionLinkData: ResolutionLinkData?,
            @SerializedName("messageError")
        val messageError: List<Any?>?,
            @SerializedName("siteUrlLite")
        val siteUrlLite: String?,
            @SerializedName("status")
        val status: String?
    ) {
        data class ResolutionLinkData(
            @SerializedName("orderList")
            val orderList: List<Order?>?
        ) {
            data class Order(
                @SerializedName("complainEligibility")
                val complainEligibility: ComplainEligibility?,
                @SerializedName("dynamicLink")
                val dynamicLink: String?,
                @SerializedName("orderID")
                val orderID: Int?,
                @SerializedName("resoList")
                val resoList: List<Reso?>?
            ) {
                data class ComplainEligibility(
                    @SerializedName("productNotReceived")
                    val productNotReceived: ProductNotReceived?,
                    @SerializedName("productReceived")
                    val productReceived: ProductReceived?
                ) {
                    data class ProductNotReceived(
                        @SerializedName("isEligible")
                        val isEligible: Boolean?,
                        @SerializedName("reason")
                        val reason: String?
                    )

                    data class ProductReceived(
                        @SerializedName("isEligible")
                        val isEligible: Boolean?,
                        @SerializedName("reason")
                        val reason: String?
                    )
                }

                data class Reso(
                    @SerializedName("id")
                    val id: Int?,
                    @SerializedName("resoTypeString")
                    val resoTypeString: String?,
                    @SerializedName("statusString")
                    val statusString: String?,
                    @SerializedName("url")
                    val url: String?
                )
            }
        }
    }
}