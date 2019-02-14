package com.tokopedia.core.network.apiservices.topads;

import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author noiz354 on 3/23/16.
 */

@Deprecated
public class TopAdsService extends BaseService<TopAdsApi> {

    public static final String TAG = TopAdsService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TopAds.URL_TOPADS;
    }

    @Override
    public TopAdsApi getApi() {
        return api;
    }
}
