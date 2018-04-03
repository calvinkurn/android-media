package com.tokopedia.posapp.product.productlist.data.mapper;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductItem;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductListData;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/17/17.
 */

public class GetProductListMapper implements Func1<Response<PosResponse<ProductListData>>, ProductListDomain> {
    @Inject
    public GetProductListMapper() {
    }

    @Override
    public ProductListDomain call(Response<PosResponse<ProductListData>> response) {
        if (response.isSuccessful() && response.body() != null) {
            return getProductListDomain(response.body());
        }

        return null;
    }

    private ProductListDomain getProductListDomain(PosResponse<ProductListData> data) {
        ProductListDomain productListDomain = new ProductListDomain();
        List<ProductDomain> domains = new ArrayList<>();

        for (ProductItem item : data.getData().getData()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setProductId(item.getProductId());
            productDomain.setProductName(item.getName());
            productDomain.setProductPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(item.getPrice().getValue(), true));
            productDomain.setProductPriceUnformatted(item.getPrice().getValue());
            productDomain.setProductUrl(item.getProductUrl());
            productDomain.setProductImage(item.getPrimaryImage().getOriginal());
            productDomain.setProductImage300(item.getPrimaryImage().getThumbnail());
            productDomain.setProductImageFull(item.getPrimaryImage().getOriginal());
            domains.add(productDomain);
        }
        productListDomain.setProductDomains(domains);
//        if(data.getPaging() != null) productListDomain.setNextUri(data.getPaging().getUriNext());

        return productListDomain;
    }
}
