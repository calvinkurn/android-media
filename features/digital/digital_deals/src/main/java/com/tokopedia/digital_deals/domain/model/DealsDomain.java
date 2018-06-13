package com.tokopedia.digital_deals.domain.model;

import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.FilterDomainModel;

import java.util.List;

public class DealsDomain {


    private List<DealsCategoryDomain> dealsCategory;

    private List<BrandDomain> dealsBrands;

    private PageDomain page;

    private List<FilterDomainModel> filters;

    public PageDomain getPage() {
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }

    public List<DealsCategoryDomain> getDealsCategory() {
        return dealsCategory;
    }

    public void setDealsCategory(List<DealsCategoryDomain> dealsCategory) {
        this.dealsCategory = dealsCategory;
    }

    public List<BrandDomain> getDealsBrands() {
        return dealsBrands;
    }

    public void setDealsBrands(List<BrandDomain> dealsBrands) {
        this.dealsBrands = dealsBrands;
    }

    public List<FilterDomainModel> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDomainModel> filters) {
        this.filters = filters;
    }
}
