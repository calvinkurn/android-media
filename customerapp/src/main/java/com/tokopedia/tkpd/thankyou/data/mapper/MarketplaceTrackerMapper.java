package com.tokopedia.tkpd.thankyou.data.mapper;

import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerMapper implements Func1<Response<GraphqlResponse<PaymentGraphql>>, String> {
    @Override
    public String call(Response<GraphqlResponse<PaymentGraphql>> graphqlResponse) {
        return null;
    }
}
