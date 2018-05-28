package com.tokopedia.digital_deals.domain.model.branddetailsmodel;

import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;

import java.util.List;

public class BrandDetailsDomain {

    private List<DealsCategoryItemDomain> dealItems;

    private BrandDomain dealBrand;


    public List<DealsCategoryItemDomain> getDealItems() {
        return dealItems;
    }

    public void setDealItems(List<DealsCategoryItemDomain> dealItems) {
        this.dealItems = dealItems;
    }

    public BrandDomain getDealBrand() {
        return dealBrand;
    }

    public void setDealBrand(BrandDomain dealBrand) {
        this.dealBrand = dealBrand;
    }

}
