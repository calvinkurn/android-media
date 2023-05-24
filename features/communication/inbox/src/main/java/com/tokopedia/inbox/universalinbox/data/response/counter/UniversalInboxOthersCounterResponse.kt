package com.tokopedia.inbox.universalinbox.data.response.counter

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxOthersCounterResponse(
    @SerializedName("talk")
    var discussionUnread: Int = Int.ZERO,

    @SerializedName("ticket")
    var helpUnread: Int = Int.ZERO,

    @SerializedName("review")
    var reviewUnread: Int = Int.ZERO
)
