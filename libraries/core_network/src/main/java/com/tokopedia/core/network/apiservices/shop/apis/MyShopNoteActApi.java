package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.myproduct.model.ActResponseModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 *         Edited by Sebast on 09/06/2016
 */

@Deprecated
public interface MyShopNoteActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_ADD_SHOP_NOTE)
    Observable<Response<TkpdResponse>> addNote(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_DELETE_SHOP_NOTE)
    Observable<Response<TkpdResponse>> deleteNote(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_SHOP_NOTE)
    Observable<Response<TkpdResponse>> editNote(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_ADD_SHOP_NOTE)
    Observable<ActResponseModel> addNote2(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_SHOP_NOTE)
    Observable<ActResponseModel> editNote2(@FieldMap Map<String, String> params);

}
