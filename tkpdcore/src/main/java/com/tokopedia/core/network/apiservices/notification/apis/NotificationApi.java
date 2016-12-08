package com.tokopedia.core.network.apiservices.notification.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface NotificationApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.FCM.UPDATE_FCM)
    Observable<Response<String>> update(@FieldMap Map<String, String> params);
}
