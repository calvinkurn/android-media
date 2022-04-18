package com.tokopedia.core.common.category.data.source.cloud

import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase.Companion.createRequestParams
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import rx.Observable

class CategoryCloud(val getCategoryLiteTreeUseCase: GetCategoryLiteTreeUseCase) {

    fun fetchDataFromNetwork(): Observable<CategoriesResponse> {
        return getCategoryLiteTreeUseCase.createObservable(createRequestParams())
    }
}