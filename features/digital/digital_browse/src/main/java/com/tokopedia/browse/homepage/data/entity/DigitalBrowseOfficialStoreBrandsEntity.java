package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseOfficialStoreBrandsEntity {
    @SerializedName("officialStoreBrands")
    @Expose
    private List<DigitalBrowsePopularBrandsEntity> officialStoreBrandList;

    public List<DigitalBrowsePopularBrandsEntity> getOfficialStoreBrandList() {
        return officialStoreBrandList;
    }

    public void setOfficialStoreBrandList(List<DigitalBrowsePopularBrandsEntity> officialStoreBrandList) {
        this.officialStoreBrandList = officialStoreBrandList;
    }
}
