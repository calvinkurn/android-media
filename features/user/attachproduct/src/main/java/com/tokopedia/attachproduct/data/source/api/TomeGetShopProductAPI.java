package com.tokopedia.attachproduct.data.source.api;

import com.tokopedia.attachproduct.data.model.TomeResponseWrapper;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 02/03/18.
 */

public interface TomeGetShopProductAPI {
    @GET(TkpdBaseURL.Tome.PATH_GET_SHOP_PRODUCT)
    Observable<Response<TomeResponseWrapper>> getShopProduct(@QueryMap Map<String, String> params);
}
