package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.apiservices.product.apis.ReviewActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public class ReviewActService extends AuthService<ReviewActApi> {
    private static final String TAG = ReviewActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ReviewActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_REVIEW_ACTION;
    }

    @Override
    public ReviewActApi getApi() {
        return api;
    }
}
