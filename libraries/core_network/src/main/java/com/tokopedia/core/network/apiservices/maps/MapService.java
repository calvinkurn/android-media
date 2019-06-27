package com.tokopedia.core.network.apiservices.maps;

import com.tokopedia.core.network.apiservices.maps.api.MapApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by kris on 9/14/17. Tokopedia
 */

@Deprecated
public class MapService extends AuthService<MapApi>{
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MapApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.MAPS_DOMAIN;
    }

    @Override
    public MapApi getApi() {
        return api;
    }
}
