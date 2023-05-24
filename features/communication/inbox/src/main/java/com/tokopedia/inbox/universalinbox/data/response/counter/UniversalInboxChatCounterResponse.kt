package com.tokopedia.inbox.universalinbox.data.response.counter

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxChatCounterResponse(
    @SerializedName("unreadsUser")
    var unreadBuyer: Int = Int.ZERO,

    @SerializedName("unreadsSeller")
    var unreadSeller: Int = Int.ZERO
)
