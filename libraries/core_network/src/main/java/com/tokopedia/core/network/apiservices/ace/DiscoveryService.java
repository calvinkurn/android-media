package com.tokopedia.core.network.apiservices.ace;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author noiz354 on 3/17/16.
 */

@Deprecated
public class DiscoveryService extends BaseService<BrowseApi> {
    public static final String TAG = DiscoveryService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(BrowseApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Ace.URL_SEARCH;
    }

    @Override
    public BrowseApi getApi() {
        return api;
    }
}
