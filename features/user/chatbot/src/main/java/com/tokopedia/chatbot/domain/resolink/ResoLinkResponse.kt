package com.tokopedia.chatbot.domain.resolink


import com.google.gson.annotations.SerializedName

data class ResoLinkResponse(
    @SerializedName("get_resolution_link")
    val getResolutionLink: GetResolutionLink?
) {
    data class GetResolutionLink(
            @SerializedName("data")
        val resolutionLinkData: ResolutionLinkData?,
            @SerializedName("messageError")
        val messageError: List<Any?>?
    ) {
        data class ResolutionLinkData(
            @SerializedName("orderList")
            val orderList: List<Order?>?
        ) {
            data class Order(
                @SerializedName("dynamicLink")
                val dynamicLink: String?,
                @SerializedName("resoList")
                val resoList: List<Reso?>?
            ) {
                data class Reso(
                    @SerializedName("id")
                    val id: String?
                )
            }
        }
    }
}