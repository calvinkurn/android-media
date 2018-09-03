package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 03/09/18.
 */

public class OfficialStoreBrandsEntity {
    @SerializedName("data")
    @Expose
    private List<PopularBrandDataEntity> data;

    public List<PopularBrandDataEntity> getData() {
        return data;
    }

    public void setData(List<PopularBrandDataEntity> data) {
        this.data = data;
    }
}
