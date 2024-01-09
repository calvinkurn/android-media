package com.tokopedia.tokopedianow.common.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.constant.AnnotationWidgetId
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetSeeAllUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel.BrandWidgetState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel

object BrandWidgetMapper {

    fun mapBrandWidget(response: GetAnnotationListResponse): BrandWidgetUiModel {
        val visitableList = mutableListOf<Visitable<*>>()
        val annotationHeader = response.annotationHeader
        val annotationList = response.annotationList
        val seeAllAppLink = annotationHeader.allPageAppLink
        val hasMore = annotationHeader.hasMore

        val header = TokoNowDynamicHeaderUiModel(
            title = annotationHeader.title,
            ctaTextLink = seeAllAppLink,
            circleSeeAll = hasMore
        )

        val brandList = annotationList.map {
            BrandWidgetItemUiModel(
                id = it.annotationID,
                imageUrl = it.imageURL,
                appLink = it.appLink
            )
        }

        visitableList.addAll(brandList)

        if(hasMore) {
            val seeAllItem = BrandWidgetSeeAllUiModel(seeAllAppLink)
            visitableList.add(seeAllItem)
        }

        return BrandWidgetUiModel(
            id = AnnotationWidgetId.BRAND,
            header = header,
            items = visitableList,
            state = BrandWidgetState.LOADED
        )
    }
}
