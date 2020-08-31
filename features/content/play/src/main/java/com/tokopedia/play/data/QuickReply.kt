package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-13.
 */
data class QuickReply(
        @SerializedName("quick_reply")
        val data: List<String> = emptyList()
)