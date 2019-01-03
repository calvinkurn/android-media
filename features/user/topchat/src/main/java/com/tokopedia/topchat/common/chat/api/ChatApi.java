package com.tokopedia.topchat.common.chat.api;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.topchat.chatlist.domain.pojo.message.MessageData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
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

public interface ChatApi {
    @GET(TkpdBaseURL.Chat.GET_MESSAGE)
    Observable<Response<DataResponse<MessageData>>> getMessage(@QueryMap Map<String, Object> requestParams);

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(TkpdBaseURL.Chat.REPLY)
    Observable<Response<TkpdResponse>> reply(@FieldMap Map<String, Object> requestParams);

    @Headers("Cookie:_SID_TOKOPEDIA_")
    @GET(TkpdBaseURL.Chat.SEARCH)
    Observable<Response<TokopediaWsV4Response>> searchChat(@QueryMap Map<String, Object> requestParams);


    @Headers("Content-Type: application/json")
    @POST(TkpdBaseURL.Chat.DELETE)
    Observable<Response<TokopediaWsV4Response>> deleteMessage(@Body JsonObject parameters);

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(TkpdBaseURL.Chat.SEND_MESSAGE)
    Observable<Response<TkpdResponse>> sendMessage(@FieldMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    Observable<Response<TokopediaWsV4Response>> getTemplate(@QueryMap Map<String, Object> parameters);

    @FormUrlEncoded
    @PUT(TkpdBaseURL.Chat.UPDATE_TEMPLATE)
    Observable<Response<TokopediaWsV4Response>> editTemplate(@Path("index") int index, @FieldMap Map<String, Object> jsonObject);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Chat.CREATE_TEMPLATE)
    Observable<Response<TokopediaWsV4Response>> createTemplate(@FieldMap Map<String, Object> parameters);

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    Observable<Response<TokopediaWsV4Response>> setTemplate(@Body JsonObject parameters);

    @HTTP(method = "DELETE", path = TkpdBaseURL.Chat.DELETE_TEMPLATE, hasBody = true)
    Observable<Response<TokopediaWsV4Response>> deleteTemplate(@Path("index") int index, @Body JsonObject object);
}
