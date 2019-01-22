package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Tkpd_Eka on 12/7/2015.
 */

@Deprecated
public class ActionService extends AuthService<ActionApi> {

    public ActionService(){}

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ActionApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_ACTION;
    }

    @Override
    public ActionApi getApi() {
        return api;
    }
}
