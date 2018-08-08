package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;


import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface PromoApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_AD_PRODUCT_FEED)
    Observable<Response<TkpdResponse>> adProductFeed(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_AD_PRODUCT_HOTLIST)
    Observable<Response<TkpdResponse>> adProductHotList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_AD_PRODUCT_SEARCH)
    Observable<Response<TkpdResponse>> adProductSearch(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_AD_SHOP_FEED)
    Observable<Response<TkpdResponse>> adShopFeed(@FieldMap Map<String, String> params);
}
