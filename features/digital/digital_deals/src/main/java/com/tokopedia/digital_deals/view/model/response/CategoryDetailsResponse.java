package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.List;

public class CategoryDetailsResponse {

    @SerializedName("grid_layout")
    @Expose
    private List<ProductItem> dealItems;

    @SerializedName("page")
    @Expose
    private Page page;

    @SerializedName("count")
    @Expose
    private int count;

    public List<ProductItem> getDealItems() {
        return dealItems;
    }

    public void setDealItems(List<ProductItem> dealItems) {
        this.dealItems = dealItems;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}