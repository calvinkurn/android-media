package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class OptOutReasonListResponse(
    @SerializedName("opt_out_reason_list")
    val listOptOutReason: List<String> = listOf()
)
