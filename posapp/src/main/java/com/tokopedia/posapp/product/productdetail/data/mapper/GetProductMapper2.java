package com.tokopedia.posapp.product.productdetail.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/19/18.
 */

public class GetProductMapper2 implements Func1<Response<DataResponse<ProductList>>, ProductDetailData> {
    @Inject
    public GetProductMapper2() {}

    @Override
    public ProductDetailData call(Response<DataResponse<ProductList>> dataResponseResponse) {
        return null;
    }
}
