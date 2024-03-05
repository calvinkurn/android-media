package com.tokopedia.shopdiscount.subsidy.model.request

import com.google.gson.annotations.SerializedName

data class DoOptOutSubsidyRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("subsidy_promotions_ids")
    var listSubsidyPromotion: List<String> = listOf(),
    @SerializedName("reason_out_program")
    var listReasonOutProgram: List<String> = listOf(),
){
    data class RequestHeader(
        @SerializedName("source")
        var source: String = "android",
        @SerializedName("usecase")
        var usecase: String = "",
    ){
        companion object {
            const val USE_CASE_SLASH_PRICE = "SP"
        }
    }
}
