package com.tokopedia.core.common.category.data.repository;

import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.di.scope.CategoryPickerScope;
import com.tokopedia.core.common.category.domain.CategoryRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
@CategoryPickerScope
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryDataSource categoryDataSource;
    private final FetchCategoryDataSource fetchCategoryDataSource;

    @Inject
    public CategoryRepositoryImpl(CategoryDataSource categoryDataSource, FetchCategoryDataSource fetchCategoryDataSource) {
        this.categoryDataSource = categoryDataSource;
        this.fetchCategoryDataSource = fetchCategoryDataSource;
    }

    @Override
    public Observable<Boolean> checkCategoryAvailable() {
        return categoryDataSource.checkCategoryAvailable();
    }

    @Override
    public Observable<List<String>> fetchCategoryDisplay(long categoryId) {
        return fetchCategoryDataSource.fetchCategoryDisplay(categoryId);
    }

    public Observable<String> getCategoryName(long categoryId) {
        return categoryDataSource.getCategoryName(categoryId);
    }
}

