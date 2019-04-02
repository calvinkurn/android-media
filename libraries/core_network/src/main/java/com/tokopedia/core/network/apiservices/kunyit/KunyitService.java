package com.tokopedia.core.network.apiservices.kunyit;

import com.tokopedia.core.network.apiservices.kunyit.apis.KunyitApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author stevenfredian on 8/2/16.
 */

@Deprecated
public class KunyitService extends AuthService<KunyitApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(KunyitApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.INBOX_DOMAIN;
    }

    @Override
    public KunyitApi getApi() {
        return api;
    }
}
