package com.tokopedia.home.service;

import com.tokopedia.core.network.apiservices.etc.apis.home.FavoriteApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * Created by m.normansyah on 1/22/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FavoritePart1Service extends BaseService<FavoriteApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(FavoriteApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_HOME;
    }

    @Override
    public FavoriteApi getApi() {
        return api;
    }
}
