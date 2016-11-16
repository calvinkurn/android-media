package com.tokopedia.core.network.apiservices.recharge;

import com.tokopedia.core.network.apiservices.recharge.apis.RechargeApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.GlobalAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import retrofit2.Retrofit;

/**
 * @author ricoharisin on 7/4/16.
 */
public class RechargeService extends GlobalAuthService<RechargeApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(RechargeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.RECHARGE_STAGING_DOMAIN;
    }

    @Override
    public RechargeApi getApi() {
        return api;
    }

    @Override
    protected String getKeyAuth() {
        return AuthUtil.KEY.KEY_WSV4;
    }
}
