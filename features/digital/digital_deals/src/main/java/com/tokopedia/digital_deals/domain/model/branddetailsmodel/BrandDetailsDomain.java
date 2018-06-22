package com.tokopedia.digital_deals.domain.model.branddetailsmodel;

import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;

import java.util.List;

public class BrandDetailsDomain {

    private List<DealsCategoryItemDomain> dealItems;

    private BrandDomain dealBrand;

    private PageDomain page;

    private int count;

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
