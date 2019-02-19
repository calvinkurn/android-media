package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 27/12/18.
 */
public class FilterQuery {

    @SerializedName("category")
    @Expose
    private List<CategoryPojo> category;

    public List<CategoryPojo> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryPojo> category) {
        this.category = category;
    }

}
