package com.tokopedia.product.manage.item.category.data.repository;


import com.tokopedia.product.manage.item.category.data.source.CategoryRecommDataSource;
import com.tokopedia.product.manage.item.category.domain.CategoryRecommRepository;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */

public class CategoryRecommRepositoryImpl implements CategoryRecommRepository {
    private final CategoryRecommDataSource categoryRecommDataSource;

    @Inject
    public CategoryRecommRepositoryImpl(CategoryRecommDataSource categoryRecommDataSource) {
        this.categoryRecommDataSource = categoryRecommDataSource;
    }

    @Override
    public Observable<CategoryRecommDataModel> fetchCategoryRecomm(String title, int row) {
        return categoryRecommDataSource.fetchCategoryRecomm(title, row);
    }
}
