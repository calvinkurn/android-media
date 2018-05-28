package com.tokopedia.digital_deals.domain.model.categorydomainmodel;

import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class CategoryDetailsDomain {

    private List<DealsCategoryItemDomain> dealItems;

    private PageDomain page;

    private List<BrandDomain> dealBrands;


    public List<DealsCategoryItemDomain> getDealItems() {
        return dealItems;
    }

    public void setDealItems(List<DealsCategoryItemDomain> dealItems) {
        this.dealItems = dealItems;
    }

    public PageDomain getPage() {
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }


    public List<BrandDomain> getDealBrands() {
        return dealBrands;
    }

    public void setDealBrands(List<BrandDomain> dealBrands) {
        this.dealBrands = dealBrands;
    }
}