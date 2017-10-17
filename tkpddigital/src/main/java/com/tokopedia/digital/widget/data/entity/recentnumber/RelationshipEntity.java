package com.tokopedia.digital.widget.data.entity.recentnumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class RelationshipEntity {

    @SerializedName("category")
    @Expose
    private CategoryEntity category;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
