package com.tokopedia.core.shopinfo.facades;

import com.tokopedia.core.discovery.model.DynamicFilterModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by errysuprayogi on 7/26/17.
 */

public interface SortApi {

    @GET("v1/dynamic_attributes")
    Observable<Response<DynamicFilterModel>> getDynamicFilter(@QueryMap Map<String, String> params);
}
