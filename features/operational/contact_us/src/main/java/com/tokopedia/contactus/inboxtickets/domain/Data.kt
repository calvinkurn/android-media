package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("show_bad_reason")
    val showBadReason: Int = 0,
    @SerializedName("bad_reason_code")
    val badReasonCode: Int = 0,
    @SerializedName("rating")
    var rating: String? = null,
    @SerializedName("comment_id")
    val commentId: String? = null
)
