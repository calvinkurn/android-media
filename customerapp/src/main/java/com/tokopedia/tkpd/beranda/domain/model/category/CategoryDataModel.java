package com.tokopedia.tkpd.beranda.domain.model.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class CategoryDataModel {
    @SerializedName("layout_sections")
    private List<CategoryLayoutSectionsModel> layoutSections;

    public List<CategoryLayoutSectionsModel> getLayoutSections() {
        return layoutSections;
    }

    public void setLayoutSections(List<CategoryLayoutSectionsModel> layoutSections) {
        this.layoutSections = layoutSections;
    }

}
