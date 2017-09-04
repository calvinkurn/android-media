package com.tokopedia.posapp.domain.model.shop;

import com.tokopedia.core.base.domain.DefaultParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/29/17.
 */

public class ShopProductListDomain implements DefaultParams {
    private java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<com.tokopedia.core.shopinfo.models.productmodel.List>();
    private String nextUri;

    public java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> getProductList() {
        return productList;
    }

    public void setProductList(java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> productList) {
        this.productList = productList;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }
}
