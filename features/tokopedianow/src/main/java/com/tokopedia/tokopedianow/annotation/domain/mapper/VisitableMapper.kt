package com.tokopedia.tokopedianow.annotation.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.annotation.domain.mapper.AllAnnotationMapper.mapToAnnotationUiModels
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse

object VisitableMapper {
    fun MutableList<Visitable<*>>.addAnnotations(response: TokoNowGetAnnotationListResponse.GetAnnotationListResponse) {
        addAll(response.mapToAnnotationUiModels())
    }

    fun MutableList<Visitable<*>>.addLoadMore() {
        add(LoadingMoreModel())
    }

    fun MutableList<Visitable<*>>.removeLoadMore() {
        removeFirst { it is LoadingMoreModel }
    }
}
