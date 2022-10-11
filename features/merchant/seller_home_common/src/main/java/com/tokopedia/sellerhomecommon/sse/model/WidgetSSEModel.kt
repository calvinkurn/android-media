package com.tokopedia.sellerhomecommon.sse.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

data class WidgetSSEModel(
    @SerializedName("event")
    val event: String = "",
    @SerializedName("message")
    val message: String = ""
)
