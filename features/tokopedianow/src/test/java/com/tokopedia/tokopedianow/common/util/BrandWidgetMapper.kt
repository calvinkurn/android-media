package com.tokopedia.tokopedianow.common.util

import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.constant.AnnotationWidgetId
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel

object BrandWidgetMapper {

    fun mapBrandWidget(response: GetAnnotationListResponse): BrandWidgetUiModel {
        val annotationHeader = response.annotationHeader

        val header = TokoNowDynamicHeaderUiModel(
            title = annotationHeader.title,
            ctaTextLink = annotationHeader.allPageAppLink,
            circleSeeAll = annotationHeader.hasMore
        )

        val brandList = response.annotationList.map {
            BrandWidgetItemUiModel(
                id = it.annotationID,
                imageUrl = it.imageURL,
                appLink = it.appLink
            )
        }

        return BrandWidgetUiModel(
            id = AnnotationWidgetId.BRAND,
            header = header,
            brandList = brandList,
            state = TokoNowLayoutState.SHOW
        )
    }
}
