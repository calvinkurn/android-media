package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ValueKey(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("value")
        val value: Any = ""
) : Serializable