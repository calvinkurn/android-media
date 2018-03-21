package com.tokopedia.posapp.product.productlist.domain.model;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/29/17.
 */

public class ProductListDomain implements DefaultParams {
    private List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<com.tokopedia.core.shopinfo.models.productmodel.List>();
    private List<ProductDomain> productDomains;
    private String nextUri;

    public List<com.tokopedia.core.shopinfo.models.productmodel.List> getProductList() {
        return productList;
    }

    public void setProductList(java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> productList) {
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
