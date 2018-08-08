package com.tokopedia.product.manage.item.imagepicker.data.repository;

import com.tokopedia.product.manage.item.imagepicker.data.model.CatalogImage;
import com.tokopedia.product.manage.item.imagepicker.data.source.CatalogImageDataSource;
import com.tokopedia.product.manage.item.imagepicker.domain.CatalogImageRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageRepositoryImpl implements CatalogImageRepository {

    private CatalogImageDataSource catalogImageDataSource;

    public CatalogImageRepositoryImpl(CatalogImageDataSource catalogImageDataSource) {
        this.catalogImageDataSource = catalogImageDataSource;
    }

    @Override
    public Observable<List<CatalogImage>> getCatalogImage(String catalogId) {
        return catalogImageDataSource.getCatalogImage(catalogId);
    }
}
