package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface NotificationApi {

    @GET(TkpdBaseURL.User.PATH_GET_NOTIFICATION)
    Observable<Response<TkpdResponse>> getNotification2(@QueryMap Map<String, Object> params);
}