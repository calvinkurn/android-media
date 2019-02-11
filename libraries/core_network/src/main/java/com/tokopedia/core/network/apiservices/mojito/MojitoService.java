package com.tokopedia.core.network.apiservices.mojito;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
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
        return TkpdBaseURL.MOJITO_DOMAIN;
    }

    @Override
    public MojitoApi getApi() {
        return api;
    }

}
