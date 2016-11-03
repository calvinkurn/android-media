package com.tokopedia.tkpd.network.apiservices.user.apis;

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
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface NotificationApi {

    @GET(TkpdBaseURL.User.PATH_GET_NOTIFICATION)
    Observable<Response<TkpdResponse>> getNotification(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_RESET_NOTIFICATION)
    Observable<Response<TkpdResponse>> resetNotification(@FieldMap Map<String, String> params);
}