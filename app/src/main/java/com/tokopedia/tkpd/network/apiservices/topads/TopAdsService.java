package com.tokopedia.tkpd.network.apiservices.topads;

import com.tokopedia.tkpd.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.tkpd.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author noiz354 on 3/23/16.
 */
public class TopAdsService extends BaseService<TopAdsApi> {

    public static final String TAG = TopAdsService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return "http://ta.tokopedia.com/promo/v1/display/";
    }

    @Override
    public TopAdsApi getApi() {
        return api;
    }
}
