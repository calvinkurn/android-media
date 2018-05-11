package com.tokopedia.digital_deals.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealsCategoryDomain {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items")
    @Expose
    private List<DealsCategoryItemDomain> items = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DealsCategoryItemDomain> getItems() {
        return items;
    }

    public void setItems(List<DealsCategoryItemDomain> items) {
        this.items = items;
    }
}
