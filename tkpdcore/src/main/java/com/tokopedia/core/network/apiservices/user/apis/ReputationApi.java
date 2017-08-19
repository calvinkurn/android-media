package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationApi {

    @GET(TkpdBaseURL.Reputation.PATH_GET_INBOX_REPUTATION)
    Observable<Response<TkpdResponse>> getInbox(@QueryMap Map<String, Object> params);

    @GET(TkpdBaseURL.Reputation.PATH_GET_DETAIL_INBOX_REPUTATION)
    Observable<Response<TkpdResponse>> getInboxDetail(@QueryMap Map<String, Object> params);

}
