package com.tokopedia.gm.statistic.data.source.cloud.model.graph;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 11/14/16.
 */
public class GetShopCategory {

    @SerializedName("shop_category")
    @Expose
    private List<Integer> categoryIdList = new ArrayList<>();

    /**
     * @return The categoryIdList
     */
    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    /**
     * @param categoryIdList The ShopCategory
     */
    public void setCategoryIdList(List<Integer> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

}
