package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.interactor.BaseCategoryUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/28/17.
 */

public class FetchCategoryDisplayUseCase extends BaseCategoryUseCase<List<String>> {

    public static final String PRODUCT_DEPARTEMENT_ID = "PRODUCT_DEPARTEMENT_ID";
    public static final int UNSELECTED = -2;

    @Inject
    public FetchCategoryDisplayUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, CategoryRepository categoryRepository) {
        super(threadExecutor, postExecutionThread, categoryRepository);
    }

    @Override
    protected Observable<List<String>> createObservableCategory(RequestParams requestParams) {
        long categoryId = requestParams.getLong(PRODUCT_DEPARTEMENT_ID, UNSELECTED);
        if (categoryId == UNSELECTED){
            throw new RuntimeException("Unselected Category");
        }

        return categoryRepository.fetchCategoryDisplay(categoryId);
    }

    public static RequestParams generateParam(long productDepartmentId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(PRODUCT_DEPARTEMENT_ID, productDepartmentId);
        return requestParam;
    }
}
