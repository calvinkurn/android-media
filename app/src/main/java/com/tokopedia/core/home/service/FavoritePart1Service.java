package com.tokopedia.core.home.service;

import com.tokopedia.core.home.api.FavoriteApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * Created by m.normansyah on 1/22/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class FavoritePart1Service extends BaseService<FavoriteApi> {

    private static final String TAG = ProductFeedService.class.getSimpleName();

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
