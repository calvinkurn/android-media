package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.model.productdetail.mosthelpful.MostHelpfulReviewResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alifa on 8/11/17.
 */

public interface ReputationReviewApi {

    String ID = "product_id";
    String SHOP_ID = "shop_id";
    String PARAM_SOURCE = "source";
    String VALUE_SNEAK_PEAK = "sneak_peak";
    String PER_PAGE = "per_page";

    @GET(TkpdBaseURL.Product.MOST_HELPFUL_REVIEW)
    Observable<Response<MostHelpfulReviewResponse>> getMostHelpfulReview(
            @QueryMap Map<String, Object> params);
}
