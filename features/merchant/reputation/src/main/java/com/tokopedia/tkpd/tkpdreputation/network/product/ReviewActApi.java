package com.tokopedia.tkpd.tkpdreputation.network.product;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ReviewActApi {

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_LIKE_DISLIKE_REVIEW_PRODUCT)
    Observable<Response<TokopediaWsV4Response>> likeDislikeReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_REPORT_REVIEW_PRODUCT)
    Observable<Response<TokopediaWsV4Response>> reportReview(@FieldMap Map<String, String> params);

}
