package com.tokopedia.discovery.categorynav.domain;

import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;
import com.tokopedia.discovery.categorynav.domain.model.ChildCategory;

import java.util.List;

import rx.Observable;

/**
 * @author by alifa on 7/7/17.
 */

public interface CategoryNavigationRepository {

    Observable<CategoryNavDomainModel> getCategoryNavigationRoot(String categoryId);

    Observable<List<Category>> getCategoryChildren(String categoryId);
}
