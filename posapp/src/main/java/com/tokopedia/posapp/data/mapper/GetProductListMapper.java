package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.pojo.ShopProductResponse;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListMapper implements Func1<Response<TkpdResponse>, ShopProductListDomain> {
    public GetProductListMapper() {

    }

    @Override
    public ShopProductListDomain call(Response<TkpdResponse> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ShopProductResponse productModel =
                    tkpdResponse.body().convertDataObj(ShopProductResponse.class);

            ShopProductListDomain shopProductListDomain = new ShopProductListDomain();
            shopProductListDomain.setProductList(productModel.getList());
            shopProductListDomain.setNextUri(productModel.getPaging().getUriNext());
            return shopProductListDomain;
        }

        return null;
    }
}
