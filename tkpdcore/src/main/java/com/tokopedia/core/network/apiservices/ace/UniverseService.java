package com.tokopedia.core.network.apiservices.ace;

import com.tokopedia.core.network.apiservices.ace.apis.UniverseApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author erry on 07/03/17.
 */

public class UniverseService extends BaseService<UniverseApi> {

    public static final String TAG = UniverseService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UniverseApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ACE_DOMAIN;
    }

    @Override
    public UniverseApi getApi() {
        return api;
    }
}
