package com.tokopedia.play_common.sse.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
data class SSEResponse(
    @SerializedName("event")
    @Expose
    val event: String = "",

    @SerializedName("message")
    @Expose
    val message: String = ""
)