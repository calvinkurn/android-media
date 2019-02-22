package com.tokopedia.core.common.category.domain;

import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRepository {
    Observable<Boolean> checkVersion();

    Observable<Boolean> checkCategoryAvailable();

    Observable<List<CategoryDomainModel>> fetchCategoryWithParent(long categoryId);

    Observable<List<CategoryLevelDomainModel>> fetchCategoryFromSelected(long categoryId);

    Observable<List<String>> fetchCategoryDisplay(long categoryId);

    Observable<Boolean> clearCache();

    Observable<String> getCategoryName(long categoryId);
}
