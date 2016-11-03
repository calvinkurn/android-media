package com.tokopedia.tkpd.network.apiservices.etc;

import com.tokopedia.tkpd.network.apiservices.etc.apis.TickerApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public class TickerService extends AuthService<TickerApi> {
    private static final String TAG = TickerService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TickerApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_TICKER;
    }

    @Override
    public TickerApi getApi() {
        return api;
    }
}
