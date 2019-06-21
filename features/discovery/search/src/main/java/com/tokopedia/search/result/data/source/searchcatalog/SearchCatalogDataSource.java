package com.tokopedia.search.result.data.source.searchcatalog;

import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.network.service.BrowseApi;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class SearchCatalogDataSource {

    private BrowseApi browseApi;
    private Func1<Response<String>, SearchCatalogModel> searchCatalogModelMapper;

    SearchCatalogDataSource(BrowseApi browseApi, Func1<Response<String>, SearchCatalogModel> searchCatalogModelMapper) {
        this.browseApi = browseApi;
        this.searchCatalogModelMapper = searchCatalogModelMapper;
    }

    public Observable<SearchCatalogModel> browseCatalogs(Map<String, Object> requestParams) {
        return browseApi.browseCatalogs(requestParams).map(searchCatalogModelMapper);
    }
}
