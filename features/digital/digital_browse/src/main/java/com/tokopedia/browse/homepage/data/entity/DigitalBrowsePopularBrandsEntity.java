package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowsePopularBrandsEntity {
    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("data")
    @Expose
    private List<DigitalBrowsePopularBrand> data;

    public List<DigitalBrowsePopularBrand> getData() {
        return data;
    }

    public void setData(List<DigitalBrowsePopularBrand> data) {
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
