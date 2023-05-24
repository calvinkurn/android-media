package com.tokopedia.inbox.universalinbox.data.response.counter

import com.google.gson.annotations.SerializedName

data class UniversalInboxCounterWrapperResponse(
    @SerializedName("notifications")
    var allCounter: UniversalInboxAllCounterResponse = UniversalInboxAllCounterResponse()
)
