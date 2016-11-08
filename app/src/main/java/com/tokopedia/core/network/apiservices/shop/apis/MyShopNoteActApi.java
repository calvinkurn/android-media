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

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.URL_MY_SHOP_NOTE_ACTION + TkpdBaseURL.Shop.PATH_EDIT_SHOP_NOTE)
    Observable<ActResponseModel> editNoteBasic(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("note_content") String noteContent,
            @Field("note_id") String noteID,
            @Field("note_title") String noteTitle
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.URL_MY_SHOP_NOTE_ACTION + TkpdBaseURL.Shop.PATH_ADD_SHOP_NOTE)
    Observable<ActResponseModel> addNoteBasic(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("note_content") String noteContent,
            @Field("terms") String terms,
            @Field("note_title") String noteTitle
    );


}
