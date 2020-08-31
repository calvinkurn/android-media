package com.tokopedia.tkpd.tkpdreputation.network.shop;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ReputationActApi {

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_DELETE_REP_REVIEW_RESPONSE)
    Observable<Response<TokopediaWsV4Response>> deleteRepReviewResponse(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_INSERT_REP_REVIEW_RESPONSE)
    Observable<Response<TokopediaWsV4Response>> insertRepReviewResponse(@FieldMap Map<String, String> params);

}
