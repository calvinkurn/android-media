package com.tokopedia.tkpd.network.apiservices.mojito;

import com.tokopedia.tkpd.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.GlobalAuthService;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * @author ricoharisin on 4/15/16.
 */
public class MojitoAuthService extends GlobalAuthService<MojitoAuthApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MojitoAuthApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.MOJITO_DOMAIN;
    }

    @Override
    public MojitoAuthApi getApi() {
        return api;
    }

    @Override
    protected String getKeyAuth() {
        return AuthUtil.KEY.KEY_MOJITO;
    }

}
