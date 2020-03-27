package com.tokopedia.home.account.favorite.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

public class FavShopItemData {
    @SerializedName("shops")
    @Expose
    List<FavShopsItem> list;

    @SerializedName("paging")
    @Expose
    PagingHandler.PagingHandlerModel pagingHandlerModel;

    public List<FavShopsItem> getList() {
        return list;
    }

    public void setList(List<FavShopsItem> list) {
        this.list = list;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    @Override
    public String toString() {
        return "FavShopItemData{" +
                "list=" + list +
                ", pagingHandlerModel=" + pagingHandlerModel +
                '}';
    }
}
