package com.tokopedia.attachproduct.data.source.service;

import com.tokopedia.attachproduct.data.source.api.AttachProductApi;
import com.tokopedia.network.constant.TkpdBaseURL;

import retrofit2.Retrofit;

/**
 * Created by Hendri on 02/03/18.
 */

public class GetShopProductService {
    AttachProductApi api;

    public GetShopProductService(AttachProductApi api) {
        this.api = api;
    }

    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(AttachProductApi.class);
    }

    protected String getBaseUrl() {
        return TkpdBaseURL.ACE_DOMAIN;
    }

    public AttachProductApi getApi() {
        return api;
    }
}