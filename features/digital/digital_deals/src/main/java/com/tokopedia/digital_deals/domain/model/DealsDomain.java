package com.tokopedia.digital_deals.domain.model;

import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class DealsDomain {


    private List<DealsCategoryDomain> dealsCategory;

    private  List<BrandDomain> dealsBrands;

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
