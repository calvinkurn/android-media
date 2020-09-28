package com.tokopedia.topads.dashboard.data.source.cloud.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BearerService;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.user.session.UserSession;

import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class TopAdsManagementService extends BearerService<TopAdsOldManagementApi> {

    public TopAdsManagementService(UserSession userSession) {
        super(userSession.getAccessToken());
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsOldManagementApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOPADS_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return null;
    }

    @Override
    public TopAdsOldManagementApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTopAdsAuth())
                .build();
    }
}