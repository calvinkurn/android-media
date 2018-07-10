package com.tokopedia.topchat.attachproduct.data.source.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.topchat.attachproduct.data.source.api.TomeGetShopProductAPI;

import retrofit2.Retrofit;

/**
 * Created by Hendri on 02/03/18.
 */

public class GetShopProductService extends AuthService<TomeGetShopProductAPI> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TomeGetShopProductAPI.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOME_DOMAIN;
    }

    @Override
    public TomeGetShopProductAPI getApi() {
        return api;
    }
}