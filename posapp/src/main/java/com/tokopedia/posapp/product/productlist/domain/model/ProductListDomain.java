package com.tokopedia.posapp.product.productlist.domain.model;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/29/17.
 */

public class ProductListDomain implements DefaultParams {
    private List<ProductDetail> productList = new ArrayList<>();
    private List<ProductDomain> productDomains;
    private String nextUri;

    public List<ProductDetail> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDetail> productList) {
        this.productList = productList;
    }

    public List<ProductDomain> getProductDomains() {
        return productDomains;
    }

    public void setProductDomains(List<ProductDomain> productDomains) {
        this.productDomains = productDomains;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }
}
