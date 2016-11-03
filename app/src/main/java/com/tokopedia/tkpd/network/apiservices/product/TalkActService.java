package com.tokopedia.tkpd.network.apiservices.product;

import com.tokopedia.tkpd.network.apiservices.product.apis.TalkActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TalkActService extends AuthService<TalkActApi>{
    private static final String TAG = TalkActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TalkActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_TALK_ACTION;
    }

    @Override
    public TalkActApi getApi() {
        return api;
    }
}
