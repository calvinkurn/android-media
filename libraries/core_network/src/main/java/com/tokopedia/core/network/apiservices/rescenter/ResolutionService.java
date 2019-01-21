package com.tokopedia.core.network.apiservices.rescenter;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by hangnadi on 3/9/17.
 */

public class ResolutionService extends BearerService<ResolutionApi> {

    public ResolutionService(String mToken) {
        super(mToken);
        this.mToken = mToken;
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.ResCenterV2.BASE_RESOLUTION;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(ResolutionApi.class);
    }

    @Override
    public ResolutionApi getApi() {
        return this.mApi;
    }
}
