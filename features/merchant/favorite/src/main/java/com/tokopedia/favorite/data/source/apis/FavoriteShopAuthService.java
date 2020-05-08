package com.tokopedia.favorite.data.source.apis;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.url.TokopediaUrl;

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
        return TokopediaUrl.Companion.getInstance().getGQL();
    }

    @Override
    public FavoriteShopApi getApi() {
        return api;
    }
}
