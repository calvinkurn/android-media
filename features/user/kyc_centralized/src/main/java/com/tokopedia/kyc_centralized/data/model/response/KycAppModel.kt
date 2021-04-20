package com.tokopedia.kyc_centralized.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KycAppModel(
        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("subtitle")
        @Expose
        var subtitle: String = "",

        @SerializedName("button")
        @Expose
        var button: String = ""
)