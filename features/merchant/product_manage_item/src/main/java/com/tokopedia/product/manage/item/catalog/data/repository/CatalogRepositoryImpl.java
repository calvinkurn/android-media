package com.tokopedia.product.manage.item.catalog.data.repository;


import com.tokopedia.product.manage.item.catalog.data.source.CatalogDataSource;
import com.tokopedia.product.manage.item.catalog.domain.CatalogRepository;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */

public class CatalogRepositoryImpl implements CatalogRepository {
    private final CatalogDataSource catalogDataSource;

    @Inject
    public CatalogRepositoryImpl(CatalogDataSource catalogDataSource) {
        this.catalogDataSource = catalogDataSource;
    }

    @Override
    public Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int row) {
        return catalogDataSource.fetchCatalog(keyword, prodDeptId, start, row);
    }
}
