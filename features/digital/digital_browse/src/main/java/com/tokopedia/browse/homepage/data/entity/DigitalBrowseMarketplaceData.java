package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseMarketplaceData {

    @SerializedName("dynamicHomeIcon")
    @Expose
    private DigitalBrowseDynamicHomeIcon categoryGroups;
    @SerializedName("popularBrandDatas")
    @Expose
    private List<DigitalBrowsePopularBrand> popularBrandDatas;

    public DigitalBrowseDynamicHomeIcon getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(DigitalBrowseDynamicHomeIcon categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public List<DigitalBrowsePopularBrand> getPopularBrandDatas() {
        return popularBrandDatas;
    }

    public void setPopularBrandDatas(List<DigitalBrowsePopularBrand> popularBrandDatas) {
        this.popularBrandDatas = popularBrandDatas;
    }
}
