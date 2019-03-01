package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ClearCategoryCacheUseCase extends UseCase<Boolean> {
    private final CategoryRepository categoryRepository;

    public ClearCategoryCacheUseCase(CategoryRepository categoryRepository) {
        super();
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return categoryRepository.clearCache();
    }
}
