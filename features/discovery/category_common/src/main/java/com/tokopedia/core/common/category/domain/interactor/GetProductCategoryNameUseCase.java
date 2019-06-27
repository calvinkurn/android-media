package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 5/18/17.
 */

public class GetProductCategoryNameUseCase extends BaseCategoryUseCase<String> {
    public static final String CAT_ID = "cat_id";

    @Inject
    public GetProductCategoryNameUseCase(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    public static RequestParams createRequestParam(long categoryId) {
        RequestParams params = RequestParams.create();
        params.putLong(CAT_ID, categoryId);
        return params;
    }

    @Override
    protected Observable<String> createObservableCategory(RequestParams requestParams) {
        final long categoryId = requestParams.getLong(CAT_ID, -1);
        return categoryRepository.getCategoryName(categoryId);
    }
}
