package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName


data class GetSellerCampaignListMetaResponse(
    @SerializedName("getSellerCampaignListMeta")
    val getSellerCampaignListMeta: CampaignMeta = CampaignMeta()
) {
    data class CampaignMeta(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("tab")
        val tab: List<Tab> = listOf(),
        @SerializedName("thematic_participation")
        val thematicParticipation: Boolean = false
    ) {
        data class ResponseHeader(
            @SerializedName("processTime")
            val processTime: Double = 0.0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )

        data class Tab(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("status")
            val status: List<Int> = listOf(),
            @SerializedName("total_campaign")
            val totalCampaign: Int = 0
        )
    }
}
