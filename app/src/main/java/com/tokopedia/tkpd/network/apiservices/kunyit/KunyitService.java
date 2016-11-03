package com.tokopedia.tkpd.network.apiservices.kunyit;

import com.tokopedia.tkpd.network.apiservices.kunyit.apis.KunyitApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author stevenfredian on 8/2/16.
 */
public class KunyitService extends BaseService<KunyitApi> {

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
