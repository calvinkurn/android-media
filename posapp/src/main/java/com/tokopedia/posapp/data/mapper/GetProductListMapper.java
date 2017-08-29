package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListMapper implements Func1<Response<TkpdResponse>, ProductModel> {
    public GetProductListMapper() {

    }

    @Override
    public ProductModel call(Response<TkpdResponse> response) {
        return null;
    }
}
