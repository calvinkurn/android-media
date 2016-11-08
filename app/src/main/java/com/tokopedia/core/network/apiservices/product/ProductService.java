package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.apiservices.product.apis.ProductApi;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 25/11/2015.
 */
public class ProductService extends AuthService<ProductApi> {
    private static final String TAG = ProductService.class.getSimpleName();

    public ProductService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ProductApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_PRODUCT;
    }

    @Override
    public ProductApi getApi() {
        return api;
    }
}
