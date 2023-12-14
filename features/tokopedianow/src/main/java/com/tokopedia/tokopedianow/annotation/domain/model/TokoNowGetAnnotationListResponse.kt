package com.tokopedia.tokopedianow.annotation.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokopedianow.common.util.StringUtil.isNotBlankAndNotZeroString

data class TokoNowGetAnnotationListResponse(
    @SerializedName("TokonowGetAnnotationList")
    val response: GetAnnotationListResponse = GetAnnotationListResponse()
) {

    companion object {
        private const val STATUS_HIDE_WIDGET = 0
        private const val STATUS_SHOW_WIDGET = 1
    }

    data class GetAnnotationListResponse(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("annotationHeader")
        val annotationHeader: AnnotationHeaderResponse = AnnotationHeaderResponse(),
        @SerializedName("status")
        val status: Int = STATUS_HIDE_WIDGET,
        @SerializedName("annotationList")
        val annotationList: List<AnnotationResponse> = listOf(),
        @SerializedName("pagination")
        val pagination: PaginationResponse = PaginationResponse()
    ) {

        fun showWidget(): Boolean {
            return status == STATUS_SHOW_WIDGET
        }

        fun isNeededToLoadMore() = pagination.hasNext && pagination.pageLastID.isNotBlankAndNotZeroString() && annotationList.isNotEmpty()
    }

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

    data class AnnotationResponse(
        @SerializedName("annotationID")
        val annotationID: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("imageURL")
        val imageURL: String = "",
        @SerializedName("appLink")
        val appLink: String = ""
    )

    data class PaginationResponse(
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("pageLastID")
        val pageLastID: String = ""
    )
}
