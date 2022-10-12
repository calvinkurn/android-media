package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerMetaResponse(
    @SerializedName("getFlashSaleListForSellerMeta")
    val getFlashSaleListForSellerMeta: GetFlashSaleListForSellerMeta = GetFlashSaleListForSellerMeta()
) {
    data class GetFlashSaleListForSellerMeta(
        @SerializedName("tab_list")
        val tabList: List<Tab> = listOf(),
        @SerializedName("ticker_non_multiloc_message")
        val tickerNonMultiLocMessage: String = ""
    ) {
        data class Tab(
            @SerializedName("display_name")
            val displayName: String = "",
            @SerializedName("tab_id")
            val tabId: String = "",
            @SerializedName("tab_name")
            val tabName: String = "",
            @SerializedName("total_campaign")
            val totalCampaign: Int = 0
        )
    }
}
