package com.tokopedia.tkpd.thankyou.data.source.api;

import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by okasurya on 12/7/17.
 */

public interface MarketplaceTrackerApi {
    @POST("graphql")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<PaymentGraphql>>> getTrackingData(@Body String requestBody);
}
