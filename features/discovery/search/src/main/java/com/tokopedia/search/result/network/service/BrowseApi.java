package com.tokopedia.search.result.network.service;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.result.data.response.SearchShopResponse;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface BrowseApi {

    @GET(SearchConstant.Ace.PATH_BROWSE_SHOP)
    Observable<Response<SearchShopResponse>> browseShops(
            @QueryMap Map<String, Object> requestParams
    );

    @GET(SearchConstant.Ace.PATH_BROWSE_CATALOG)
    Observable<Response<String>> browseCatalogs(
            @QueryMap Map<String, Object> requestParams
    );

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE)
    Observable<Response<String>> getDynamicAttribute(
            @QueryMap Map<String, Object> requestParams
    );

    @GET(SearchConstant.Ace.PATH_GET_DYNAMIC_ATTRIBUTE_V4)
    Observable<Response<String>> getDynamicAttributeV4(
            @QueryMap  Map<String, Object> requestParams
    );
}
