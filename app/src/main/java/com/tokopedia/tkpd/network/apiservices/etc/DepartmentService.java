package com.tokopedia.tkpd.network.apiservices.etc;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.etc.apis.DepartmentApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class DepartmentService extends AuthService<DepartmentApi> {
    private static final String TAG = DepartmentService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(DepartmentApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Etc.URL_DEPARTMENT;
    }

    @Override
    public DepartmentApi getApi() {
        return api;
    }
}
