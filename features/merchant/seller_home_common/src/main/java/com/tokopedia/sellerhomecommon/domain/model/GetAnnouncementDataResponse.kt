package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class GetAnnouncementDataResponse(
    @SerializedName("fetchAnnouncementWidgetData")
    val fetchAnnouncementWidgetData: FetchAnnouncementWidgetData? = FetchAnnouncementWidgetData()
)

data class FetchAnnouncementWidgetData(
    @SerializedName("data")
    val `data`: List<AnnouncementWidgetDataModel>? = emptyList()
)

data class AnnouncementWidgetDataModel(
    @SerializedName("dataKey")
    val dataKey: String? = String.EMPTY,
    @SerializedName("errorMsg")
    val errorMsg: String? = String.EMPTY,
    @SerializedName("showWidget")
    val showWidget: Boolean? = true,
    @SerializedName("subtitle")
    val subtitle: String? = String.EMPTY,
    @SerializedName("title")
    val title: String? = String.EMPTY,
    @SerializedName("imageUrl")
    val imageUrl: String? = String.EMPTY,
    @SerializedName("widgetDataSign")
    val widgetDataSign: String = String.EMPTY,
    @SerializedName("button")
    val cta: AnnouncementCta? = AnnouncementCta()
)

data class AnnouncementCta(
    @SerializedName("applink")
    val appLink: String? = String.EMPTY
)