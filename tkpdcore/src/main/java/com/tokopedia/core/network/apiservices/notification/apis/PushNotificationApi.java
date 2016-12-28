package com.tokopedia.core.network.apiservices.notification.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

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
    @POST(TkpdBaseURL.FCM.UPDATE_FCM)
    Observable<Response<TkpdResponse>> updateToken(@FieldMap Map<String, String> params);
}
