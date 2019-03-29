package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.FavShopsItem;
import com.tokopedia.core.var.ShopItem;

import java.util.List;

/**
 * Created by naveengoyal on 5/8/18.
 */

@Deprecated
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

    public void setPagingHandlerModel(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        this.pagingHandlerModel = pagingHandlerModel;
    }

    @Override
    public String toString() {
        return "FavShopItemData{" +
                "list=" + list +
                ", pagingHandlerModel=" + pagingHandlerModel +
                '}';
    }
}
