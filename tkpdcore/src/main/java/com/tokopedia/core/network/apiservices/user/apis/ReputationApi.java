package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @POST(TkpdBaseURL.Reputation.PATH_SEND_REPUTATION_SMILEY)
    @FormUrlEncoded
    Observable<Response<TkpdResponse>> sendSmiley(@FieldMap Map<String, Object> params);

    @POST(TkpdBaseURL.Reputation.PATH_SEND_REVIEW_VALIDATE)
    @FormUrlEncoded
    Observable<Response<TkpdResponse>> sendReviewValidate(@FieldMap Map<String, Object> params);

    @POST(TkpdBaseURL.Reputation.PATH_SEND_REVIEW_SUBMIT)
    @FormUrlEncoded
    Observable<Response<TkpdResponse>> sendReviewSubmit(@FieldMap Map<String, Object> params);
}
