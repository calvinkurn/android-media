package com.tokopedia.recentview.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentViewData {

    @SerializedName("items")
    @Expose
    List<ProductItem> list;

    public List<ProductItem> getList() {
        return list;
    }

    public void setList(List<ProductItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ProductItemData{" +
                "list=" + list +
                '}';
    }

}
