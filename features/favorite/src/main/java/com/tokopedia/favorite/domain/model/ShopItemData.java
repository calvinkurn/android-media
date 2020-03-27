package com.tokopedia.home.account.favorite.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ShopItem;

import java.util.List;

/**
 * Created by m.normansyah on 27/11/2015.
 */

@Deprecated
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
