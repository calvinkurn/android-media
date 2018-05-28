package com.tokopedia.digital_deals.domain.model.searchdomainmodel;

import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;

import java.util.List;


public class SearchDomainModel {
    private List<DealsCategoryItemDomain> deals;
    private List<FilterDomainModel> filters;
    private PageDomain page;

    public List<DealsCategoryItemDomain> getDeals() {
        return deals;
    }

    public void setEvents(List<DealsCategoryItemDomain> deals) {
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
