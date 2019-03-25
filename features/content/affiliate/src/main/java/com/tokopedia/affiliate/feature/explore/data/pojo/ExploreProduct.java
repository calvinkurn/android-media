package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreProduct {

    @SerializedName("pagination")
    @Expose
    private ExplorePaginationPojo pagination;

    @SerializedName("products")
    @Expose
    private List<ExploreProductPojo> products;

    public ExplorePaginationPojo getPagination() {
        return pagination;
    }

    public void setPagination(ExplorePaginationPojo pagination) {
        this.pagination = pagination;
    }

    public List<ExploreProductPojo> getProducts() {
        return products;
    }

    public void setProducts(List<ExploreProductPojo> products) {
        this.products = products;
    }

}
