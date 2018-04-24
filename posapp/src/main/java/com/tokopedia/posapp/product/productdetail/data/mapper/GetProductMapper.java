package com.tokopedia.posapp.product.productdetail.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/10/17.
 *
 * Will be deleted, changed by {@link GetProductDetailMapper}
 */
@Deprecated
public class GetProductMapper implements Func1<Response<DataResponse<ProductDetailData>>, ProductDetailData>{
    @Inject
    public GetProductMapper() {

    }

    @Override
    public ProductDetailData call(Response<DataResponse<ProductDetailData>> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null && tkpdResponse.body().getData() != null) {
            return tkpdResponse.body().getData();
//            ProductDetailData productDetailData =
//                    tkpdResponse.body().convertDataObj(ProductDetailData.class);
//
//            if(productDetailData != null) {
//                return productDetailData;
//            }
        }
        return null;
    }
}
