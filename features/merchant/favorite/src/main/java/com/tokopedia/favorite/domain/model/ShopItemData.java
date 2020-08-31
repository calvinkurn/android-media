package com.tokopedia.favorite.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;

import java.util.List;

public class ShopItemData {
    @SerializedName("list")
    @Expose
    List<ShopItem> list;

    @SerializedName("paging")
    @Expose
    PagingHandler.PagingHandlerModel pagingHandlerModel;

    public List<ShopItem> getList() {
        return list;
    }

    public void setList(List<ShopItem> list) {
        this.list = list;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    @Override
    public String toString() {
        return "ShopItemData{" +
                "list=" + list +
                ", pagingHandlerModel=" + pagingHandlerModel +
                '}';
    }
}
