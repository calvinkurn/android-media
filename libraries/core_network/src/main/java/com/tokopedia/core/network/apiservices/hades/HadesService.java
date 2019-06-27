package com.tokopedia.core.network.apiservices.hades;

import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.apiservices.search.apis.HotListApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Alifa on 2/22/2017.
 */

@Deprecated
public class HadesService extends AuthService<HadesApi> {
    private static final String TAG = HadesService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(HadesApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HadesCategory.URL_HADES;
    }

    @Override
    public HadesApi getApi() {
        return api;
    }
}

