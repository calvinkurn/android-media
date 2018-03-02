package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.base.data.pojo.Paging;

import java.util.List;

/**
 * Created by okasurya on 10/17/17.
 */

public class ProductData {
    @SerializedName("products")
    @Expose
    private List<ProductItem> products;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("search_url")
    @Expose
    private String searchUrl;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
