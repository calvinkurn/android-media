package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface MyShopEtalaseActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EVENT_SHOP_ADD_ETALASE)
    Observable<Response<TkpdResponse>> addEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EVENT_SHOP_DELETE_ETALASE)
    Observable<Response<TkpdResponse>> deleteEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EVENT_SHOP_REORDER_ETALASE)
    Observable<Response<TkpdResponse>> reorderEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EVENT_SHOP_EDIT_ETALASE)
    Observable<Response<TkpdResponse>> editEtalase(@FieldMap Map<String, String> params);
}
