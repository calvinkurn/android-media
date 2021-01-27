package com.tokopedia.tkpd.tkpdreputation.review.product.data.source;

import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public interface ReviewProductApi {

    @GET(ReputationBaseURL.PATH_GET_REVIEW_PRODUCT_LIST)
    Observable<Response<DataResponse<DataResponseReviewProduct>>> getReviewProductList(@QueryMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_REVIEW_HELPFUL_LIST)
    Observable<Response<DataResponse<DataResponseReviewHelpful>>> getReviewHelpfulList(@QueryMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_REVIEW_PRODUCT_RATING)
    Observable<Response<DataResponse<DataResponseReviewStarCount>>> getReviewStarCount(@QueryMap Map<String, String> params);
}
