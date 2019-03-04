
package com.tokopedia.digital.categorylist.data.cloud.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("layout_sections")
    private List<LayoutSection> mLayoutSections;

    public List<LayoutSection> getLayoutSections() {
        return mLayoutSections;
    }

    public void setLayoutSections(List<LayoutSection> layout_sections) {
        mLayoutSections = layout_sections;
    }

}
