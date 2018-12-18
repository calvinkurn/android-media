package com.tokopedia.core.gcm.network;


import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface PushNotificationApi {
    @FormUrlEncoded
    @POST("/api/gcm/update")
    Observable<Response<TokopediaWsV4Response>> updateToken(@FieldMap Map<String, String> params);
}
