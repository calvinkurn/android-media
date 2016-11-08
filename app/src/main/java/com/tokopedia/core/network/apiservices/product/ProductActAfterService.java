package com.tokopedia.core.network.apiservices.product;

import com.tokopedia.core.myproduct.api.UploadImageProduct;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author noiz354 on 8/3/16.
 */
public class ProductActAfterService extends AuthService<UploadImageProduct> {
    String baseUrl;

    public ProductActAfterService(String baseUrl){
        super(baseUrl, true);
        this.baseUrl = baseUrl;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageProduct.class);
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl+"/web-service/v4/action/upload-image-helper/";
    }

    @Override
    public UploadImageProduct getApi() {
        return api;
    }
}
