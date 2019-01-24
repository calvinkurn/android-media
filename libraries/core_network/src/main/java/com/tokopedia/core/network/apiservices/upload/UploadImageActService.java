package com.tokopedia.core.network.apiservices.upload;

import com.tokopedia.core.network.apiservices.upload.apis.UploadImageActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author hangnadi on 2/22/16.
 */
public class UploadImageActService extends AuthService<UploadImageActApi> {

    private static final String TAG = UploadImageActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Upload.URL_UPLOAD_IMAGE_ACTION;
    }

    @Override
    public UploadImageActApi getApi() {
        return api;
    }
}
