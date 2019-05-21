package com.tokopedia.search.result.network.service;

import com.tokopedia.discovery.common.constants.SearchConstant;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface DynamicFilterService {

    @GET(SearchConstant.BaseUrl.PATH_GET_DYNAMIC_ATTRIBUTE)
    Observable<Response<String>> getDynamicAttribute(
            @QueryMap Map<String, Object> requestParams
    );

    @GET(SearchConstant.BaseUrl.PATH_GET_DYNAMIC_ATTRIBUTE_V4)
    Observable<Response<String>> getDynamicAttributeV4(
            @QueryMap  Map<String, Object> requestParams
    );
}
