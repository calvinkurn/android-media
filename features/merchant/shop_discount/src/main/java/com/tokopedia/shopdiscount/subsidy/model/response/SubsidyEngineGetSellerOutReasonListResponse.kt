package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class SubsidyEngineGetSellerOutReasonListResponse(
    @SerializedName("SubsidyEngineGetSellerOutReasonList")
    val subsidyEngineGetSellerOutReasonList: SubsidyEngineGetSellerOutReasonList = SubsidyEngineGetSellerOutReasonList()
) {
    data class SubsidyEngineGetSellerOutReasonList(
        @SerializedName("ReasonOptions")
        val listReasonOption: List<ReasonOption> = listOf()
    ) {
        data class ReasonOption(
            @SerializedName("Reason")
            val reason: String = ""
        )
    }

}
