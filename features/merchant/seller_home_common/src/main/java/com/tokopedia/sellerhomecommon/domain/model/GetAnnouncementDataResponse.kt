package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAnnouncementDataResponse(
        @Expose
        @SerializedName("fetchAnnouncementWidgetData")
        val fetchAnnouncementWidgetData: FetchAnnouncementWidgetData? = FetchAnnouncementWidgetData()
)

data class FetchAnnouncementWidgetData(
        @Expose
        @SerializedName("data")
        val `data`: List<AnnouncementWidgetDataModel>? = emptyList()
)

data class AnnouncementWidgetDataModel(
        @Expose
        @SerializedName("dataKey")
        val dataKey: String? = "",
        @Expose
        @SerializedName("errorMsg")
        val errorMsg: String? = "",
        @Expose
        @SerializedName("showWidget")
        val showWidget: Boolean? = true,
        @Expose
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @Expose
        @SerializedName("title")
        val title: String? = "",
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String? = "",
        @Expose
        @SerializedName("button")
        val cta: AnnouncementCta? = AnnouncementCta()
)

data class AnnouncementCta(
        @Expose
        @SerializedName("applink")
        val appLink: String? = ""
)