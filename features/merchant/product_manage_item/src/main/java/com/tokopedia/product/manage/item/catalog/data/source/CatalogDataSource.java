package com.tokopedia.product.manage.item.catalog.data.source;


import com.tokopedia.product.manage.item.catalog.data.source.cloud.CatalogCloud;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CatalogDataSource {
    private final CatalogCloud catalogCloud;

    @Inject
    public CatalogDataSource(CatalogCloud catalogCloud) {
        this.catalogCloud = catalogCloud;
    }

    public Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int row) {
        return catalogCloud.fetchData(keyword, prodDeptId, start, row);
    }
}
