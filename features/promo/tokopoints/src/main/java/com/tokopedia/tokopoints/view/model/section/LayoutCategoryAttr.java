
package com.tokopedia.tokopoints.view.model.section;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LayoutCategoryAttr {

    @SerializedName("categoryTokopointsList")
    @Expose
    private List<CategoryTokopointsList> categoryTokopointsList = null;

    public List<CategoryTokopointsList> getCategoryTokopointsList() {
        return categoryTokopointsList;
    }

    public void setCategoryTokopointsList(List<CategoryTokopointsList> categoryTokopointsList) {
        this.categoryTokopointsList = categoryTokopointsList;
    }

}
