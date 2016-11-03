package com.tokopedia.tkpd.network.apiservices.product;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.services.AuthService;
import com.tokopedia.tkpd.network.apiservices.product.apis.ProductActApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 04/12/2015.
 */
public class ProductActService extends AuthService<ProductActApi> {
    private static final String TAG = ProductActService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ProductActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_PRODUCT_ACTION;
    }

    @Override
    public ProductActApi getApi() {
        return api;
    }
}
