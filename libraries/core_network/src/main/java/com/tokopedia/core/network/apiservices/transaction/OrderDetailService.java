package com.tokopedia.core.network.apiservices.transaction;

import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.network.apiservices.transaction.apis.OrderDetailApi;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

@Deprecated
public class OrderDetailService extends AuthService<OrderDetailApi>{


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(OrderDetailApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getWS();
    }

    @Override
    public OrderDetailApi getApi() {
        return api;
    }
}
