package com.tokopedia.tkpd.network.apiservices.upload;

import com.tokopedia.tkpd.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 10/12/2015.
 *         more documentation visit https://wiki.tokopedia.net/Generate_Host_WSv4
 */
public class GenerateHostActService extends AuthService<GeneratedHostActApi> {
    private static final String TAG = GenerateHostActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GeneratedHostActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Upload.URL_GENERATE_HOST_ACTION;
    }

    @Override
    public GeneratedHostActApi getApi() {
        return api;
    }
}
