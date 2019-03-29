package com.tokopedia.core.network.apiservices.chat.api;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

@Deprecated
public interface ChatApi {
    @GET(TkpdBaseURL.Chat.GET_EXISTING_CHAT)
    Observable<Response<TkpdResponse>> getExistingChat(@QueryMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_MESSAGE)
    Observable<Response<TkpdResponse>> getMessage(@QueryMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_REPLY)
    Observable<Response<TkpdResponse>> getReply(@Path("msgId") String messageId, @QueryMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_USER_CONTACT)
    Observable<String> getUserContact(@Path("msgIds") String messageIds, @QueryMap Map<String, Object> requestParams);

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(TkpdBaseURL.Chat.REPLY)
    Observable<Response<TkpdResponse>> reply(@FieldMap Map<String, Object> requestParams);

    @Headers("Cookie:_SID_TOKOPEDIA_")
    @GET(TkpdBaseURL.Chat.LISTEN_WEBSOCKET)
    Observable<Response<TkpdResponse>> listenWebSocket(@QueryMap Map<String, Object> mapParam);


    @Headers("Cookie:_SID_TOKOPEDIA_")
    @GET(TkpdBaseURL.Chat.SEARCH)
    Observable<Response<TkpdResponse>> searchChat(@QueryMap Map<String, Object> requestParams);


    @Headers("Content-Type: application/json")
    @POST(TkpdBaseURL.Chat.DELETE)
    Observable<Response<TkpdResponse>> deleteMessage(@Body JsonObject parameters);

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(TkpdBaseURL.Chat.SEND_MESSAGE)
    Observable<Response<TkpdResponse>> sendMessage(@FieldMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_TOPCHAT_NOTIFICATION)
    Observable<Response<TkpdResponse>> getNotification(@QueryMap Map<String, Object> params);

    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    Observable<Response<TkpdResponse>> getTemplate(@QueryMap Map<String, Object> parameters);

    @FormUrlEncoded
    @PUT(TkpdBaseURL.Chat.UPDATE_TEMPLATE)
    Observable<Response<TkpdResponse>> editTemplate(@Path("index") int index, @FieldMap Map<String, Object> jsonObject);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Chat.CREATE_TEMPLATE)
    Observable<Response<TkpdResponse>> createTemplate(@FieldMap Map<String, Object> parameters);

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    Observable<Response<TkpdResponse>> setTemplate(@Body JsonObject parameters);

    @HTTP(method = "DELETE", path = TkpdBaseURL.Chat.DELETE_TEMPLATE, hasBody = true)
    Observable<Response<TkpdResponse>> deleteTemplate(@Path("index") int index, @Body JsonObject object);
}
