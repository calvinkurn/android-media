package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OfficialStore(
        @SerializedName("error")
        @Expose
        val error: String = "",
        @SerializedName("status")
        @Expose
        val status: String = ""
)