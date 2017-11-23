package com.tokopedia.discovery.categorynav.data.repository;

import com.tokopedia.discovery.categorynav.data.source.CategoryNavigationDataSource;
import com.tokopedia.discovery.categorynav.domain.CategoryNavigationRepository;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

import rx.Observable;

/**
 * @author by alifa on 7/7/17.
 */

public class CategoryNavigationRepositoryImpl implements CategoryNavigationRepository {

    private final CategoryNavigationDataSource categoryNavigationDataSource;

    public CategoryNavigationRepositoryImpl(CategoryNavigationDataSource categoryNavigationDataSource) {
        this.categoryNavigationDataSource = categoryNavigationDataSource;
    }

    @Override
    public Observable<CategoryNavDomainModel> getCategoryNavigationRoot(String categoryId) {
        return categoryNavigationDataSource.getRootCategory(categoryId);
    }

    @Override
    public Observable<List<Category>> getCategoryChildren(String categoryId) {
        return categoryNavigationDataSource.getCategoryChildren(categoryId);
    }
}
