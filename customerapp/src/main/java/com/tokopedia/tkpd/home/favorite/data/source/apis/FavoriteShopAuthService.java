package com.tokopedia.tkpd.home.favorite.data.source.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by naveengoyal on 5/7/18.
 */

public class FavoriteShopAuthService extends AuthService<FavoriteShopApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
            api = retrofit.create(FavoriteShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public FavoriteShopApi getApi() {
        return api;
    }
}
