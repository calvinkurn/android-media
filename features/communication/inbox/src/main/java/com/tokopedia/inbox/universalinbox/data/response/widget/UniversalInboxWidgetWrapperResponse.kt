package com.tokopedia.inbox.universalinbox.data.response.widget

import com.google.gson.annotations.SerializedName

data class UniversalInboxWidgetWrapperResponse(
    @SerializedName("chatInboxWidgetMeta")
    val chatInboxWidgetMeta: UniversalInboxWidgetMetaResponse = UniversalInboxWidgetMetaResponse()
)
