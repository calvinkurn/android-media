package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.myproduct.model.NoteDetailModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface NotesApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_NOTES_DETAIL)
    Observable<Response<TkpdResponse>> getNotesDetail(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_NOTES_DETAIL)
    Observable<NoteDetailModel> getNotesDetail2(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.URL_NOTES + TkpdBaseURL.Shop.PATH_GET_NOTES_DETAIL)
    Observable<NoteDetailModel> getNotesDetailBasic(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("shop_domain") String shopDomain,// 9
            @Field("shop_id") String shopId,// 9
            @Field("note_id") String noteId,// 9
            @Field("terms") String terms
    );
}
