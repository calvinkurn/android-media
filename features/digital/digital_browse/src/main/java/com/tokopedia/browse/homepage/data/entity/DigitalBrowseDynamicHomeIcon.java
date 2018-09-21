package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseDynamicHomeIcon {
    @SerializedName("categoryGroup")
    @Expose
    private List<DigitalBrowseCategoryGroupEntity> dynamicHomeCategoryGroupEntities;

    public List<DigitalBrowseCategoryGroupEntity> getDynamicHomeCategoryGroupEntities() {
        return dynamicHomeCategoryGroupEntities;
    }

    public void setDynamicHomeCategoryGroupEntities(List<DigitalBrowseCategoryGroupEntity> dynamicHomeCategoryGroupEntities) {
        this.dynamicHomeCategoryGroupEntities = dynamicHomeCategoryGroupEntities;
    }
}
