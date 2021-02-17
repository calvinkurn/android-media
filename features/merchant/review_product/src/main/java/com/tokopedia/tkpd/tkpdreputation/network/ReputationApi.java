package com.tokopedia.tkpd.tkpdreputation.network;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ReputationApi {

    @POST(ReputationBaseURL.PATH_DELETE_REVIEW_RESPONSE)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> deleteReviewResponse(@FieldMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_LIKE_DISLIKE_REVIEW)
    Observable<Response<TokopediaWsV4Response>> getLikeDislikeReview(@QueryMap Map<String, Object> parameters);

    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW)
    @FormUrlEncoded
    Observable<Response<TokopediaWsV4Response>> likeDislikeReview(@FieldMap Map<String, String> params);

}
