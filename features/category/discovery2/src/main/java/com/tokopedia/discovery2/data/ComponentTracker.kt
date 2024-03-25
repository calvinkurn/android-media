package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class ComponentTracker(
    @SerializedName("request_id")
    val requestId: String? = String.EMPTY,
    @SerializedName("session_id")
    val sessionId: String? = String.EMPTY,
    @SerializedName("log_id")
    val logId: String? = String.EMPTY,
    @SerializedName("pagename")
    val recommendationPageName: String? = String.EMPTY
)
