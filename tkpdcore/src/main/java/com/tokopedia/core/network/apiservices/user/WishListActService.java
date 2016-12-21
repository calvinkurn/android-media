package com.tokopedia.core.network.apiservices.user;

import com.tokopedia.core.network.apiservices.user.apis.WishListActionApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 04/12/2015.
 */
public class WishListActService extends AuthService<WishListActionApi> {
    private static final String TAG = WishListActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(WishListActionApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_WISH_LIST_ACTION;
    }

    @Override
    public WishListActionApi getApi() {
        return api;
    }
}
