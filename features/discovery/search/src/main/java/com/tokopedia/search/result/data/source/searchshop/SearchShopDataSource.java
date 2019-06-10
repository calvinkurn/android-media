package com.tokopedia.search.result.data.source.searchshop;

import com.tokopedia.search.result.data.response.SearchShopResponse;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.network.service.BrowseApi;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class SearchShopDataSource {

    private BrowseApi browseApi;
    private Func1<Response<SearchShopResponse>, SearchShopModel> searchShopModelMapper;

    SearchShopDataSource(BrowseApi browseApi, Func1<Response<SearchShopResponse>, SearchShopModel> searchShopModelMapper) {
        this.browseApi = browseApi;
        this.searchShopModelMapper = searchShopModelMapper;
    }

    public Observable<SearchShopModel> browseShops(Map<String, Object> requestParams) {
        return browseApi.browseShops(requestParams).map(searchShopModelMapper);
    }
}
