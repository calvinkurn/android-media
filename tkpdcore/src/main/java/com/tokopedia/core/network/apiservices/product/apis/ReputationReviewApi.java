package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.productdetail.mosthelpful.MostHelpfulReviewResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alifa on 8/11/17.
 */

public interface ReputationReviewApi {

    String ID = "product_id";
    String SHOP_ID = "shop_id";


    @GET(TkpdBaseURL.Product.MOST_HELPFUL_REVIEW)
    Observable<Response<MostHelpfulReviewResponse>> getMostHelpfulReview(
            @Query(ID) String id,
            @Query(SHOP_ID) String shopId);
}
