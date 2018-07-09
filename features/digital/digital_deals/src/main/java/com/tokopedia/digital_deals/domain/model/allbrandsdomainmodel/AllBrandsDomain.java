package com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class AllBrandsDomain {

    @SerializedName("page")
    private PageDomain page;

    @SerializedName("brands")
    private List<BrandDomain> brands;

    public PageDomain getPage() {
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }

    public List<BrandDomain> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandDomain> brands) {
        this.brands = brands;
    }
}