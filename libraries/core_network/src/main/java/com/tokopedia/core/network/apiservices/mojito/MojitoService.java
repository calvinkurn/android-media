package com.tokopedia.core.network.apiservices.mojito;

import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author ricoharisin on 4/15/16.
 */

@Deprecated
public class MojitoService extends BaseService<MojitoApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MojitoApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TokopediaUrl.Companion.getInstance().getMOJITO();
    }

    @Override
    public MojitoApi getApi() {
        return api;
    }

}
