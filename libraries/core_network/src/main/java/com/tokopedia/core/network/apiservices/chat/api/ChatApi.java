package com.tokopedia.core.network.apiservices.chat.api;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

@Deprecated
public interface ChatApi {
    @GET(TkpdBaseURL.Chat.GET_MESSAGE)
    Observable<Response<TkpdResponse>> getMessage(@QueryMap Map<String, Object> requestParams);

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(TkpdBaseURL.Chat.REPLY)
    Observable<Response<TkpdResponse>> reply(@FieldMap Map<String, Object> requestParams);

    @GET(TkpdBaseURL.Chat.GET_TOPCHAT_NOTIFICATION)
    Observable<Response<TkpdResponse>> getNotification(@QueryMap Map<String, Object> params);

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    Observable<Response<TkpdResponse>> setTemplate(@Body JsonObject parameters);
}
