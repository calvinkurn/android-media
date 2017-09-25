package com.tokopedia.core.category.data.repository;

import com.tokopedia.core.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.category.domain.CategoryRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */
@Deprecated
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryVersionDataSource categoryVersionDataSource;

    public CategoryRepositoryImpl(CategoryVersionDataSource categoryVersionDataSource) {
        this.categoryVersionDataSource = categoryVersionDataSource;
    }

    @Override
    public Observable<Boolean> checkVersion() {
        return categoryVersionDataSource.checkVersion();
    }
}
