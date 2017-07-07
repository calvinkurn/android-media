package com.tokopedia.discovery.categorynav.domain;

import com.tokopedia.discovery.categorynav.domain.model.CategoryNavDomainModel;

import rx.Observable;

/**
 * @author by alifa on 7/7/17.
 */

public interface CategoryNavigationRepository {

    Observable<CategoryNavDomainModel> getCategoryNavigationRoot(String categoryId);
}
