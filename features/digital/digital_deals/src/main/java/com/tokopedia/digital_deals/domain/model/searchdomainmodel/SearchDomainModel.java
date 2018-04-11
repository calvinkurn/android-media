package com.tokopedia.digital_deals.domain.model.searchdomainmodel;

import com.tokopedia.digital_deals.domain.model.DealsItemDomain;

import java.util.List;


public class SearchDomainModel {
    private List<DealsItemDomain> deals;
    private List<FilterDomainModel> filters;
    private PageDomain page;

    public List<DealsItemDomain> getDeals() {
        return deals;
    }

    public void setEvents(List<DealsItemDomain> deals) {
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


}
