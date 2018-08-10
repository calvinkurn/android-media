package com.tokopedia.attachproduct.data.source.service;
import com.tokopedia.attachproduct.data.source.api.TomeGetShopProductAPI;
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.network.constant.TkpdBaseURL;

import retrofit2.Retrofit;

/**
 * Created by Hendri on 02/03/18.
 */

public class GetShopProductService {
    TomeGetShopProductAPI api;

    public GetShopProductService(TomeGetShopProductAPI api) {
        this.api = api;
    }

    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TomeGetShopProductAPI.class);
    }

    protected String getBaseUrl() {
        return TkpdBaseURL.TOME_DOMAIN;
    }

    public TomeGetShopProductAPI getApi() {
        return api;
    }
}