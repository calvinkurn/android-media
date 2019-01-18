package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/10/18.
 */
public class ExploreData {

    @SerializedName("topadsExploreAffiliateProduct")
    @Expose
    private ExploreQuery exploreProduct;

    @SerializedName("topadsGetExploreCategory")
    @Expose
    private FilterQuery filter;

    @SerializedName("topadsGetExploreSort")
    private SortQuery sort;

    public ExploreQuery getExploreProduct() {
        return exploreProduct;
    }

    public void setExploreProduct(ExploreQuery exploreProduct) {
        this.exploreProduct = exploreProduct;
    }

    public FilterQuery getFilter() {
        return filter;
    }

    public void setFilter(FilterQuery filter) {
        this.filter = filter;
    }

    public SortQuery getSort() {
        return sort;
    }

    public void setSort(SortQuery sort) {
        this.sort = sort;
    }

}
