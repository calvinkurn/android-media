package com.tokopedia.search.result.data.repository.shoplist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.search.result.data.source.searchshop.SearchShopDataSource;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import java.util.Map;

import rx.Observable;

final class SearchShopRepository implements Repository<SearchShopModel> {

    private SearchShopDataSource searchShopDataSource;

    SearchShopRepository(SearchShopDataSource searchShopDataSource) {
        this.searchShopDataSource = searchShopDataSource;
    }

    @Override
    public Observable<SearchShopModel> query(Map<String, Object> parameters) {
        return searchShopDataSource.browseShops(parameters);
    }
}
