package com.tokopedia.attachproduct.data.source.api;

import com.tokopedia.attachproduct.data.model.AceResponseWrapper;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 02/03/18.
 */

public interface AttachProductApi {
    @GET(TkpdBaseURL.Ace.PATH_SEARCH_PRODUCT)
    Observable<Response<AceResponseWrapper>> getShopProduct(@QueryMap Map<String, String> params);
}
