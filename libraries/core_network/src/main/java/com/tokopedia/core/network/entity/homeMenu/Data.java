
package com.tokopedia.core.network.entity.homeMenu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Deprecated
public class Data {

    @SerializedName("layout_sections")
    private List<com.tokopedia.core.network.entity.homeMenu.LayoutSection> mLayoutSections;

    public List<com.tokopedia.core.network.entity.homeMenu.LayoutSection> getLayoutSections() {
        return mLayoutSections;
    }

    public void setLayoutSections(List<com.tokopedia.core.network.entity.homeMenu.LayoutSection> layout_sections) {
        mLayoutSections = layout_sections;
    }

}
