package com.tokopedia.inbox.universalinbox.data.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxWidgetWrapperResponse(
    @SerializedName("chatInboxWidgetMeta")
    val chatInboxWidgetMeta: UniversalInboxWidgetMetaResponse = UniversalInboxWidgetMetaResponse()
)

data class UniversalInboxWidgetMetaResponse(
    @SerializedName("metadata")
    var metaData: List<UniversalInboxWidgetDataResponse> = listOf()
)

data class UniversalInboxWidgetDataResponse(
    @SerializedName("icon")
    val icon: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("subtext")
    val subtext: String = "",

    @SerializedName("androidApplink")
    val applink: String = "",

    @SerializedName("type")
    val type: Int = Int.ZERO,

    @SerializedName("isDynamic")
    var isDynamic: Boolean = false
)
