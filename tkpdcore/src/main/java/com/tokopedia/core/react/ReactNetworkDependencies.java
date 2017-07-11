package com.tokopedia.core.react;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.react.data.ReactNetworkRepositoryImpl;
import com.tokopedia.core.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.core.react.data.factory.ReactNetworkFactory;
import com.tokopedia.core.react.domain.ReactNetworkRepository;

import retrofit2.Retrofit;

/**
 * @author ricoharisin .
 * origanlly i want to use dagger, but since our base dagger implemenatation not support module
 * without activity so i decided to posponed it
 */

public class ReactNetworkDependencies {

    public ReactNetworkRepository createReactNetworkRepository() {
        return new ReactNetworkRepositoryImpl(provideReactNetworkAuthFactory(), provideReactNetworkFactory());
    }

    private ReactNetworkFactory provideReactNetworkFactory() {
        return new ReactNetworkFactory(provideRetrofitNoAuth().create(CommonService.class));
    }

    private ReactNetworkAuthFactory provideReactNetworkAuthFactory() {
        return new ReactNetworkAuthFactory(provideRetrofitAuth().create(CommonService.class));
    }

    private Retrofit provideRetrofitNoAuth() {
        return RetrofitFactory.createBasicRetrofit(TkpdBaseURL.BASE_DOMAIN)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(provideOkHttpRetryPolicy())
                        .buildClientNoAuth())
                .build();
    }

    private Retrofit provideRetrofitAuth() {
        return RetrofitFactory.createBasicRetrofit(TkpdBaseURL.BASE_DOMAIN)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(provideOkHttpRetryPolicy())
                        .buildClientDefaultAuth())
                .build();
    }

    private OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }
}
