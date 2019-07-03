package com.tokopedia.core.network.apiservices.product;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.network.apiservices.product.apis.ReputationReviewApi;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by alifa on 8/11/17.
 */

@Deprecated
public class ReputationReviewService extends AuthService<ReputationReviewApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReputationReviewApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getWS();
    }

    @Override
    public ReputationReviewApi getApi() {
        return api;
    }

}
