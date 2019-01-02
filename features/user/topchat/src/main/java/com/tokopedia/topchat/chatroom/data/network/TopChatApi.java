package com.tokopedia.topchat.chatroom.data.network;

import com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus.GetUserStatusResponsePojo;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendri on 31/07/18.
 */
public interface TopChatApi {
    @GET("/js/userlogin")
    Observable<Response<GetUserStatusResponsePojo>> getUserStatus(@QueryMap Map<String, String> params);

    @GET("/js/shoplogin")
    Observable<Response<GetUserStatusResponsePojo>> getShopStatus(@QueryMap Map<String, String> params );
}
