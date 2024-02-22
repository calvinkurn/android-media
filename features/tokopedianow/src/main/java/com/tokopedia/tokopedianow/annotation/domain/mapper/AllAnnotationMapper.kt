package com.tokopedia.tokopedianow.annotation.domain.mapper

import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel

object AllAnnotationMapper {
    fun GetAnnotationListResponse.mapToAnnotationUiModels(): List<AnnotationUiModel> {
        return annotationList.map {
            // tempImageUrl logic will handle broken image issue, will delete the logic if from media solves the issue
            val tempImageUrl = it.imageURL.ifBlank { null }
            AnnotationUiModel(
                id = it.annotationID,
                name = it.name,
                imageUrl = tempImageUrl,
                appLink = it.appLink
            )
        }
    }
}
