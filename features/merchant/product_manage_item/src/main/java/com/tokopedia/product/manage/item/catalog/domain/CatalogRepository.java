package com.tokopedia.product.manage.item.catalog.domain;

import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CatalogRepository {
    Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int rows);
}
