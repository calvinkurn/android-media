package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class PreValidateRedeemBaseValue(
    @SerializedName("is_valid")
    var isValid: Int = 0
)
