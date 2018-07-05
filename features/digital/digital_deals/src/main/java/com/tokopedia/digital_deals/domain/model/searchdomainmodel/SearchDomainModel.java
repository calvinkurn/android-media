package com.tokopedia.digital_deals.domain.model.searchdomainmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;

import java.util.List;


public class SearchDomainModel {

    @SerializedName("grid_layout")
    @Expose
    private List<DealsCategoryItemDomain> deals;

    private List<FilterDomainModel> filters;

    @SerializedName("page")
    @Expose
    private PageDomain page;

    @SerializedName("count")
    @Expose
    private int count;

    public List<DealsCategoryItemDomain> getDeals() {
        return deals;
    }

    public void setDeals(List<DealsCategoryItemDomain> deals) {
        this.deals = deals;
    }

    public List<FilterDomainModel> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDomainModel> filters) {
        this.filters = filters;
    }

    public PageDomain getPage() {
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
