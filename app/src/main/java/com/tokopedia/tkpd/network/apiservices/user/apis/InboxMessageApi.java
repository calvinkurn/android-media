package com.tokopedia.tkpd.network.apiservices.user.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface InboxMessageApi {

    @GET(TkpdBaseURL.User.PATH_GET_INBOX_MESSAGE)
    Observable<Response<TkpdResponse>> getMessage(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_INBOX_DETAIL_MESSAGE)
    Observable<Response<TkpdResponse>> getDetail(@QueryMap Map<String, String> params);
}
