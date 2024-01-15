package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class DoOptOutResponse(
    @SerializedName("success")
    val isSuccess: Boolean = false
)
