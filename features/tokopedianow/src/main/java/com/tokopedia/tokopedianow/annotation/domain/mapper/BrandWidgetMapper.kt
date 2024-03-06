package com.tokopedia.tokopedianow.annotation.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.presentation.constant.AnnotationWidgetId
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetItemUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetSeeAllUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel.BrandWidgetState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getVisitableId

object BrandWidgetMapper {

    fun MutableList<Visitable<*>>.addBrandWidget() {
        add(BrandWidgetUiModel(id = AnnotationWidgetId.BRAND))
    }

    fun MutableList<Visitable<*>>.mapBrandWidget(response: GetAnnotationListResponse) {
        val item = first { it is BrandWidgetUiModel } as BrandWidgetUiModel
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
                name = it.name,
                imageUrl = it.imageURL,
                appLink = it.appLink
            )
        }

        visitableList.addAll(brandList)

        if(hasMore) {
            val seeAllItem = BrandWidgetSeeAllUiModel(seeAllAppLink)
            visitableList.add(seeAllItem)
        }

        val visitable = item.copy(
            header = header,
            items = visitableList,
            state = BrandWidgetState.LOADED
        )

        val index = indexOf(item)
        this[index] = visitable
    }

    fun MutableList<Visitable<*>>.mapBrandWidgetLoading(id: String) {
        firstOrNull { it.getVisitableId() == id }?.let {
            val item = it as BrandWidgetUiModel
            val index = indexOf(it)
            this[index] = item.copy(
                state = BrandWidgetState.LOADING
            )
        }
    }

    fun MutableList<Visitable<*>>.mapBrandWidgetError() {
        firstOrNull { it is BrandWidgetUiModel }?.let {
            val item = it as BrandWidgetUiModel
            val index = indexOf(it)
            this[index] = item.copy(
                state = BrandWidgetState.ERROR
            )
        }
    }

    fun MutableList<Visitable<*>>.removeBrandWidget() {
        firstOrNull { it is BrandWidgetUiModel }?.let {
            remove(it)
        }
    }
}
