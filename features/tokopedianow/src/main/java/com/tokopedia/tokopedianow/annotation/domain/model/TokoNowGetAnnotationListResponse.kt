package com.tokopedia.tokopedianow.annotation.domain.model

import com.google.gson.annotations.SerializedName

data class TokoNowGetAnnotationListResponse(
    @SerializedName("TokonowGetAnnotationList")
    val response: GetAnnotationListResponse = GetAnnotationListResponse(),
) {

    data class GetAnnotationListResponse(
        @SerializedName("widgetHeader")
        val widgetHeader: WidgetHeaderResponse = WidgetHeaderResponse(),
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("widgetList")
        val widgetList: List<WidgetListResponse> = listOf(),
        @SerializedName("pagination")
        val pagination: PaginationResponse = PaginationResponse()
    )

    data class WidgetHeaderResponse(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("hasMore")
        val hasMore: Boolean = false,
        @SerializedName("allPageAppLink")
        val allPageAppLink: String = "",
        @SerializedName("allPageWebLink")
        val allPageWebLink: String = ""

    )

    data class WidgetListResponse(
        @SerializedName("annotationID")
        val annotationID: String = "",
        @SerializedName("imageURL")
        val imageURL: String = "",
        @SerializedName("appLink")
        val appLink: String = "",
        @SerializedName("webLink")
        val webLink: String = ""
    )

    data class PaginationResponse(
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("pageLastID")
        val pageLastID: String = ""
    )
}
