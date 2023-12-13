package com.tokopedia.tokopedianow.annotation.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.constant.AnnotationWidgetId
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel

object BrandWidgetMapper {

    fun MutableList<Visitable<*>>.addBrandWidget() {
        add(BrandWidgetUiModel(id = AnnotationWidgetId.BRAND))
    }

    fun MutableList<Visitable<*>>.mapBrandWidget(response: GetAnnotationListResponse) {
        val item = first { it is BrandWidgetUiModel } as BrandWidgetUiModel
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

        val visitable = item.copy(
            header = header,
            brandList = brandList,
            state = TokoNowLayoutState.SHOW
        )

        val index = indexOf(item)
        this[index] = visitable
    }

    fun MutableList<Visitable<*>>.removeBrandWidget() {
        firstOrNull { it is BrandWidgetUiModel }?.let {
            remove(it)
        }
    }
}
