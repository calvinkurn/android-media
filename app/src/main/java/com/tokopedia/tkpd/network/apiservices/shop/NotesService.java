package com.tokopedia.tkpd.network.apiservices.shop;

import com.tokopedia.tkpd.network.apiservices.shop.apis.NotesApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class NotesService extends AuthService<NotesApi> {
    private static final String TAG = NotesService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(NotesApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_NOTES;
    }

    @Override
    public NotesApi getApi() {
        return api;
    }
}
