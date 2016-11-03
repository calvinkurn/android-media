package com.tokopedia.tkpd.network.apiservices.shop.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * MyShopEtalaseApi
 *
 * @author Angga.Prasetiyo on 02/12/2015.
 */
public interface MyShopEtalaseApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<TkpdResponse>> getShopEtalase(@QueryMap Map<String, String> params);
}
