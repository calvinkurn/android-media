package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.EtalaseResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 9/18/17.
 */

public interface TomeApi {
    @GET(TkpdBaseURL.Tome.PATH_GET_SHOP_ETALASE)
    Observable<Response<EtalaseResponse>> getShopEtalase(@QueryMap Map<String, String> params);
}
