package com.tokopedia.kyc_centralized.data.model

import com.google.gson.annotations.SerializedName

data class KycHeaderDataModel(
    @SerializedName("message")
    var message: MutableList<String?> = mutableListOf(),
    @SerializedName("error_code")
    var errorCode: String? = "",
)
