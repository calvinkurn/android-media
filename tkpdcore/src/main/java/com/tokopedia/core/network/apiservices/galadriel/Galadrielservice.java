package com.tokopedia.core.network.apiservices.galadriel;

import com.tokopedia.core.network.apiservices.product.apis.ReputationReviewApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * Created by alifa on 9/13/17.
 */

public class Galadrielservice extends BaseService<GaladrielApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GaladrielApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.GALADRIEL;
    }

    @Override
    public GaladrielApi getApi() {
        return api;
    }

}
