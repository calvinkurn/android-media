package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.product.ProductItem;
import com.tokopedia.posapp.data.pojo.product.ProductData;
import com.tokopedia.posapp.data.pojo.product.ProductList;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
import com.tokopedia.posapp.domain.model.product.ProductListDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/17/17.
 */

public class GetGatewayProductListMapper implements Func1<Response<TkpdResponse>, ProductListDomain> {
    private Gson gson;

    public GetGatewayProductListMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public ProductListDomain call(Response<TkpdResponse> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ProductList productList = gson.fromJson(
                    tkpdResponse.body().getStringData(),
                    ProductList.class
            );

            if(productList != null
                    && productList.getData() != null) {
                return getProductListDomain(productList.getData());
            }
        }

        return null;
    }

    private ProductListDomain getProductListDomain(ProductData data) {
        ProductListDomain productListDomain = new ProductListDomain();
        List<ProductDomain> domains = new ArrayList<>();

        for(ProductItem item : data.getProducts()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setProductId(item.getProductId());
            productDomain.setProductName(item.getName());
            productDomain.setProductPrice(item.getPrice());
            productDomain.setProductPriceUnformatted(CurrencyFormatHelper.convertRupiahToDouble(item.getPrice()));
            productDomain.setProductUrl(item.getUrl());
            productDomain.setProductImage(item.getImageUrl());
            productDomain.setProductImage300(item.getImageUrl());
            productDomain.setProductImageFull(item.getImageUrl700());
            domains.add(productDomain);
        }
        productListDomain.setProductDomains(domains);
        if(data.getPaging() != null) productListDomain.setNextUri(data.getPaging().getUriNext());

        return productListDomain;
    }
}
