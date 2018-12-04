package com.tokopedia.recentview.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentViewData {

    @SerializedName("list")
    @Expose
    List<ProductItem> list;

    @SerializedName("paging")
    @Expose
    PagingData pagingHandlerModel;

    public List<ProductItem> getList() {
        return list;
    }

    public void setList(List<ProductItem> list) {
        this.list = list;
    }

    public PagingData getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    public void setPagingHandlerModel(PagingData pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }

    @Override
    public String toString() {
        return "ProductItemData{" +
                "list=" + list +
                ", pagingHandlerModel=" + pagingHandlerModel +
                '}';
    }

}
