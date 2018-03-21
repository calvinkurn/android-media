package com.tokopedia.posapp.product.management.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 3/20/18.
 */

public class GetProductListManagementMapper implements Func1<Response<TkpdResponse>, ProductListDomain> {
    @Override
    public ProductListDomain call(Response<TkpdResponse> tkpdResponseResponse) {
        return null;
    }
}
