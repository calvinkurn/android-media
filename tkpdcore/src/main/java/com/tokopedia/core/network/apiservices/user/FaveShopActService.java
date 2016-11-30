package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.FaveShopActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 02/12/2015.
 */
public class FaveShopActService extends AuthService<FaveShopActApi> {
    private static final String TAG = FaveShopActService.class.getSimpleName();

    public FaveShopActService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(FaveShopActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_FAVE_SHOP_ACTION;
    }

    @Override
    public FaveShopActApi getApi() {
        return api;
    }
}
