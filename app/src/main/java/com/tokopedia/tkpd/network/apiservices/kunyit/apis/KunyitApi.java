package com.tokopedia.tkpd.network.apiservices.kunyit.apis;


import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * KunyitApi
 * Created by stevenfredian on 8/2/16.
 */
public interface KunyitApi {


    //    Observable<Response<TkpdResponse>> getTalkProduct(@QueryMap Map<String, String> params);
    @GET(TkpdBaseURL.Kunyit.GET_INBOX_TALK)
    Observable<Response<TkpdResponse>> getInboxTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.Kunyit.GET_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> getProductTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.Kunyit.GET_COMMENT_TALK)
    Observable<Response<TkpdResponse>> getCommentTalk(@QueryMap Map<String, String> stringStringMap);

    @GET(TkpdBaseURL.Kunyit.GET_INBOX_TALK_DETAIL)
    Observable<Response<TkpdResponse>> getTalkDetail(@QueryMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.ADD_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> addProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.ADD_COMMENT_TALK)
    Observable<Response<TkpdResponse>> addCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.DELETE_COMMENT_TALK)
    Observable<Response<TkpdResponse>> deleteCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.REPORT_COMMENT_TALK)
    Observable<Response<TkpdResponse>> reportCommentTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.FOLLOW_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> followProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.DELETE_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> deleteProductTalk(@FieldMap Map<String, String> stringStringMap);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Kunyit.REPORT_PRODUCT_TALK)
    Observable<Response<TkpdResponse>> reportProductTalk(@FieldMap Map<String, String> stringStringMap);


//    @FormUrlEncoded
//    @POST(TkpdBaseURL.Kunyit.GET_INBOX_TALK)
//    Observable<Response<TkpdResponse>> getInboxTalk(@FieldMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST(TkpdBaseURL.Kunyit.ADD_TALK_PRODUCT)
//    Observable<Response<TkpdResponse>> addTalkProduct(@FieldMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST(TkpdBaseURL.Kunyit.ADD_TALK_COMMENT)
//    Observable<Response<TkpdResponse>> addTalkComment(@FieldMap Map<String, String> params);
}
