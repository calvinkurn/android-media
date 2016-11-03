package com.tokopedia.tkpd.network.apiservices.recharge;

import com.tokopedia.tkpd.network.apiservices.recharge.apis.RechargeApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.GlobalAuthService;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

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
        return TkpdBaseURL.RECHARGE_API_DOMAIN;
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
