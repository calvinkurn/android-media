package com.tokopedia.posapp.product.productdetail.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/10/17.
 */

public class GetProductMapper implements Func1<Response<TkpdResponse>, ProductDetailData>{
    @Inject
    public GetProductMapper() {

    }

    @Override
    public ProductDetailData call(Response<TkpdResponse> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ProductDetailData productDetailData =
                    tkpdResponse.body().convertDataObj(ProductDetailData.class);

            if(productDetailData != null) {
                return productDetailData;
            }
        }
        return null;
    }
}
