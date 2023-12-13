package com.tokopedia.tokopedianow.annotation.domain.mapper

import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel

object AllAnnotationMapper {
    fun GetAnnotationListResponse.mapToAnnotationUiModel(): List<AnnotationUiModel> {
        return annotationList.map {
            AnnotationUiModel(
                id = it.annotationID,
                name = it.name,
                imageUrl = it.imageURL,
                appLink = it.appLink
            )
        }
    }
}
