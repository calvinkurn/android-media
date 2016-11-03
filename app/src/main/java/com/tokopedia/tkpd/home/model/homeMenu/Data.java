
package com.tokopedia.tkpd.home.model.homeMenu;

import java.util.List;
import com.google.gson.annotations.SerializedName;

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
