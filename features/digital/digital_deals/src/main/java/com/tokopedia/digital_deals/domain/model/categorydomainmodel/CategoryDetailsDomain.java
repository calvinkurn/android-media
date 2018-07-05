package com.tokopedia.digital_deals.domain.model.categorydomainmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class CategoryDetailsDomain {

    @SerializedName("grid_layout")
    @Expose
    private List<DealsCategoryItemDomain> dealItems;

    @SerializedName("page")
    @Expose
    private PageDomain page;

    @SerializedName("count")
    @Expose
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}