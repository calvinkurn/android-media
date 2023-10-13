package com.tokopedia.inbox.universalinbox.data.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxWrapperResponse(
    @SerializedName("chatInboxWidgetMeta")
    val chatInboxMenu: UniversalInboxMenuAndWidgetMetaResponse = UniversalInboxMenuAndWidgetMetaResponse()
)

data class UniversalInboxMenuAndWidgetMetaResponse(
    @SerializedName("metadata")
    var widgetMenu: List<UniversalInboxWidgetDataResponse> = listOf(),

    @SerializedName("inboxMenu")
    var inboxMenu: List<UniversalInboxMenuDataResponse> = listOf()
) {
    var shouldShowLocalLoad = false
}

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

data class UniversalInboxMenuDataResponse(
    @SerializedName("icon")
    val icon: String = "0",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("appLink")
    val applink: String = "",

    @SerializedName("label")
    val label: UniversalInboxMenuLabelDataResponse = UniversalInboxMenuLabelDataResponse(),

    @SerializedName("type")
    val type: String = ""
)

data class UniversalInboxMenuLabelDataResponse(
    @SerializedName("color")
    val color: String = "",

    @SerializedName("text")
    val text: String = ""
)
