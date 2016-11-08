package com.tokopedia.core.recharge.model.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ricoharisin on 7/14/16.
 */
public class CategoryData {

    @SerializedName("data")
    @Expose
    private List<Category> data;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

}
