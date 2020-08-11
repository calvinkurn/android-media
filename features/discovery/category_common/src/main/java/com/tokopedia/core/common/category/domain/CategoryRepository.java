package com.tokopedia.core.common.category.domain;

import com.tokopedia.core.common.category.domain.model.CategoryDomainModel;
import com.tokopedia.core.common.category.domain.model.CategoryLevelDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRepository {
    Observable<Boolean> checkCategoryAvailable();

    Observable<List<String>> fetchCategoryDisplay(long categoryId);

    Observable<String> getCategoryName(long categoryId);
}
