package com.tokopedia.inbox.universalinbox.data.response.widget

import com.google.gson.annotations.SerializedName

data class UniversalInboxWidgetMetaResponse(
    @SerializedName("metadata")
    var metaData: List<UniversalInboxWidgetDataResponse> = listOf()
)
