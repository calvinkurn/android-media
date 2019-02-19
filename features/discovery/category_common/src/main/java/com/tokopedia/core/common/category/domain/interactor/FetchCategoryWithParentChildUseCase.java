package com.tokopedia.core.common.category.domain.interactor;

import com.tokopedia.core.common.category.data.source.db.CategoryDataBase;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */
public class FetchCategoryWithParentChildUseCase extends BaseCategoryUseCase<List<CategoryDomainModel>> {

    public static final int UNSELECTED = -2;
    public static final String CATEGORY_PARENT = "CATEGORY_PARENT";

    @Inject
    public FetchCategoryWithParentChildUseCase(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }

    @Override
    protected Observable<List<CategoryDomainModel>> createObservableCategory(RequestParams requestParams) {
        long categoryId = requestParams.getLong(CATEGORY_PARENT, UNSELECTED);
        return categoryRepository.fetchCategoryWithParent(categoryId);
    }

    public static RequestParams generateLevelOne() {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_PARENT, CategoryDataBase.LEVEL_ONE_PARENT);
        return requestParam;
    }

    public static RequestParams generateFromParent(long categoryId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_PARENT, categoryId);
        return requestParam;
    }
}