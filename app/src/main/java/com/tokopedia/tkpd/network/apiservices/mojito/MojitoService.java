package com.tokopedia.tkpd.network.apiservices.mojito;

import com.tokopedia.tkpd.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author ricoharisin on 4/15/16.
 */
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
