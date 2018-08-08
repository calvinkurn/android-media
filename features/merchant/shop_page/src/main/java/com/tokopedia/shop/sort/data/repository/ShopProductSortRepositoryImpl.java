package com.tokopedia.shop.sort.data.repository;

import com.tokopedia.shop.sort.data.source.cloud.ShopProductSortCloudDataSource;
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductSortRepositoryImpl implements ShopProductSortRepository {

    private ShopProductSortCloudDataSource shopFilterCloudDataSource;

    @Inject
    public ShopProductSortRepositoryImpl(ShopProductSortCloudDataSource shopFilterCloudDataSource) {
        this.shopFilterCloudDataSource = shopFilterCloudDataSource;
    }

    @Override
    public Observable<List<ShopProductSort>> getShopProductFilter() {
        return shopFilterCloudDataSource.getDynamicFilter();
    }
}
