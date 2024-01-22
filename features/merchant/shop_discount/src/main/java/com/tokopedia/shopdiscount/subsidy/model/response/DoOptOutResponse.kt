package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class DoOptOutResponse(
    @SerializedName("is_success")
    val isSuccess: Boolean = false
)
