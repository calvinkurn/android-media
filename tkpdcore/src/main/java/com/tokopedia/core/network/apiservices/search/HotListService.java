package com.tokopedia.core.network.apiservices.search;

import com.tokopedia.core.network.apiservices.search.apis.HotListApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class HotListService extends AuthService<HotListApi> {
    private static final String TAG = HotListService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(HotListApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Search.URL_HOT_LIST;
    }

    @Override
    public HotListApi getApi() {
        return api;
    }
}
