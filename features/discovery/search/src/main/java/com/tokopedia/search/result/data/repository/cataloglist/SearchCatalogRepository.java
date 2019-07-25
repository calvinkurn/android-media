package com.tokopedia.search.result.data.repository.cataloglist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.search.result.data.source.searchcatalog.SearchCatalogDataSource;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;

import java.util.Map;

import rx.Observable;

final class SearchCatalogRepository implements Repository<SearchCatalogModel> {

    private final SearchCatalogDataSource searchCatalogDataSource;

    SearchCatalogRepository(SearchCatalogDataSource searchCatalogDataSource) {
        this.searchCatalogDataSource = searchCatalogDataSource;
    }

    @Override
    public Observable<SearchCatalogModel> query(Map<String, Object> parameters) {
        return searchCatalogDataSource.browseCatalogs(parameters);
    }
}
