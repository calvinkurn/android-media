package com.tokopedia.kyc_centralized.data.model

import com.google.gson.annotations.SerializedName

data class KycAppModel(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("subtitle")
    var subtitle: String = "",
    @SerializedName("button")
    var button: String = "",
)
