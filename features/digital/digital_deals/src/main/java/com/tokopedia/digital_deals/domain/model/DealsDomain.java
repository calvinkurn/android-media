package com.tokopedia.digital_deals.domain.model;

import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class DealsDomain {


    private List<DealsCategoryDomain> dealsCategory;

    private List<BrandDomain> dealsBrands;

    private PageDomain page;

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
}
