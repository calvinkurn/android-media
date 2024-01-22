package com.tokopedia.tokopedianow.data

import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse

object AllAnnotationDataFactory {
    fun createFirstPageData() = TokoNowGetAnnotationListResponse.GetAnnotationListResponse(
        annotationHeader = TokoNowGetAnnotationListResponse.AnnotationHeaderResponse(
            title = "Semua brand"
        ),
        annotationList = listOf(
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "11",
                name = "annotation 1",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "12",
                name = "annotation 2",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "13",
                name = "annotation 3",
                appLink = "tokopedia://now/all-annotation"
            )
        ),
        pagination = TokoNowGetAnnotationListResponse.PaginationResponse(
            hasNext = false,
            pageLastID = "0"
        )
    )

    fun createFirstPageLoadMoreData() = TokoNowGetAnnotationListResponse.GetAnnotationListResponse(
        annotationHeader = TokoNowGetAnnotationListResponse.AnnotationHeaderResponse(
            title = "Semua brand",
            hasMore = true
        ),
        annotationList = listOf(
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "11",
                name = "annotation 1",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "12",
                name = "annotation 2",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "13",
                name = "annotation 3",
                appLink = "tokopedia://now/all-annotation"
            )
        ),
        pagination = TokoNowGetAnnotationListResponse.PaginationResponse(
            hasNext = true,
            pageLastID = "333"
        )
    )

    fun createSecondPageNoLoadMoreData(
        annotationList: List<TokoNowGetAnnotationListResponse.AnnotationResponse> = listOf(
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "14",
                name = "annotation 4",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "15",
                name = "annotation 5",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "16",
                name = "annotation 6",
                appLink = "tokopedia://now/all-annotation"
            )
        )
    ) = TokoNowGetAnnotationListResponse.GetAnnotationListResponse(
        annotationHeader = TokoNowGetAnnotationListResponse.AnnotationHeaderResponse(
            title = "Semua brand",
            hasMore = true
        ),
        annotationList = annotationList,
        pagination = TokoNowGetAnnotationListResponse.PaginationResponse(
            hasNext = false,
            pageLastID = "3221"
        )
    )

    fun createSecondPageHasLoadMoreData(
        annotationList: List<TokoNowGetAnnotationListResponse.AnnotationResponse> = listOf(
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "14",
                name = "annotation 4",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "15",
                name = "annotation 5",
                appLink = "tokopedia://now/all-annotation"
            ),
            TokoNowGetAnnotationListResponse.AnnotationResponse(
                annotationID = "16",
                name = "annotation 6",
                appLink = "tokopedia://now/all-annotation"
            )
        )
    ) = TokoNowGetAnnotationListResponse.GetAnnotationListResponse(
        annotationHeader = TokoNowGetAnnotationListResponse.AnnotationHeaderResponse(
            title = "Semua brand",
            hasMore = true
        ),
        annotationList = annotationList,
        pagination = TokoNowGetAnnotationListResponse.PaginationResponse(
            hasNext = true,
            pageLastID = "3221"
        )
    )
}
