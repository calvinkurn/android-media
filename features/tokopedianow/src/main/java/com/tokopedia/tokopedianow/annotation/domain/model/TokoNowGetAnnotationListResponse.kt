package com.tokopedia.tokopedianow.annotation.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class TokoNowGetAnnotationListResponse(
    @SerializedName("TokonowGetAnnotationList")
    val response: GetAnnotationListResponse = GetAnnotationListResponse(),
) {

    data class GetAnnotationListResponse(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("annotationHeader")
        val annotationHeader: AnnotationHeaderResponse = AnnotationHeaderResponse(),
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("annotationList")
        val annotationList: List<AnnotationListResponse> = listOf(),
        @SerializedName("pagination")
        val pagination: PaginationResponse = PaginationResponse()
    )

    data class AnnotationHeaderResponse(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("hasMore")
        val hasMore: Boolean = false,
        @SerializedName("allPageAppLink")
        val allPageAppLink: String = "",
        @SerializedName("allPageWebLink")
        val allPageWebLink: String = ""

    )

    data class AnnotationListResponse(
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
