package com.tokopedia.core.base.common.service;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.etc.apis.home.ProductFeedApi.SEARCH_V2_4_PRODUCT;

/**
 * @author Kulomady on 12/9/16.
 */

public interface AceService {

    @GET(SEARCH_V2_4_PRODUCT)
    Observable<Response<String>> getProductFeed(@QueryMap Map<String, Object> params);

}
