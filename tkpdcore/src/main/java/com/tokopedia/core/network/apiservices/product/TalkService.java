package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.apiservices.product.apis.TalkApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TalkService extends AuthService<TalkApi> {
    private static final String TAG = TalkService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TalkApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_TALK;
    }

    @Override
    public TalkApi getApi() {
        return api;
    }
}
