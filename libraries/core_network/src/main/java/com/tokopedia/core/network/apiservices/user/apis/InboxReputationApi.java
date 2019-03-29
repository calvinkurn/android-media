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
public interface InboxReputationApi {

    @GET(TkpdBaseURL.User.PATH_GET_INBOX_REPUTATION)
    Observable<Response<TkpdResponse>> getInbox(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_LIST_REPUTATION_REVIEW)
    Observable<Response<TkpdResponse>> getListReview(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_SINGLE_REPUTATION_REVIEW)
    Observable<Response<TkpdResponse>> getSingleReview(@QueryMap Map<String, String> params);
}
