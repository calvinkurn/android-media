package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.ShopProductResponse;
import com.tokopedia.posapp.domain.model.shop.ProductListDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListMapper implements Func1<Response<TkpdResponse>, ProductListDomain> {
    @Inject
    public GetProductListMapper() {

    }

    @Override
    public ProductListDomain call(Response<TkpdResponse> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ShopProductResponse productModel =
                    tkpdResponse.body().convertDataObj(ShopProductResponse.class);

            ProductListDomain productListDomain = new ProductListDomain();
            productListDomain.setProductList(productModel.getList());
            productListDomain.setNextUri(productModel.getPaging().getUriNext());
            return productListDomain;
        }

        return null;
    }
}
