package com.tokopedia.liveness.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessAppsModel(
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