package com.tokopedia.core.common.category.data.source.cloud;

import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase;
import com.tokopedia.core.common.category.domain.model.CategoriesResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */
public class CategoryCloud {

    private final GetCategoryLiteTreeUseCase getCategoryLiteTreeUseCase;

    @Inject
    public CategoryCloud(GetCategoryLiteTreeUseCase getCategoryLiteTreeUseCase) {
        this.getCategoryLiteTreeUseCase = getCategoryLiteTreeUseCase;
    }

    public Observable<CategoriesResponse> fetchDataFromNetwork() {
        return getCategoryLiteTreeUseCase.createObservable(GetCategoryLiteTreeUseCase.Companion.createRequestParams());
    }
}