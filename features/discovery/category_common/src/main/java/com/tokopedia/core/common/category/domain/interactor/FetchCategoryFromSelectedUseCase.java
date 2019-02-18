package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class FetchCategoryFromSelectedUseCase extends BaseCategoryUseCase<List<CategoryLevelDomainModel>> {

    public static final String INIT_SELECTED = "INIT_SELECTED";
    public static final int INIT_UNSELECTED = -1;

    @Inject
    public FetchCategoryFromSelectedUseCase(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    @Override
    protected Observable<List<CategoryLevelDomainModel>> createObservableCategory(RequestParams requestParams) {
        long initSelected = requestParams.getLong(INIT_SELECTED, INIT_UNSELECTED);
        if (initSelected == INIT_UNSELECTED) {
            throw new RuntimeException("Init category is unselected");
        }
        return categoryRepository.fetchCategoryFromSelected(initSelected);
    }

    public static RequestParams generateParam(long initSelected) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(INIT_SELECTED, initSelected);
        return requestParam;
    }
}
