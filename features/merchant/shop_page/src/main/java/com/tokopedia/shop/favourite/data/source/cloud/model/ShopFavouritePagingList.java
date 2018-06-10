
package com.tokopedia.shop.favourite.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.PagingDetail;

import java.util.ArrayList;
import java.util.List;

public class ShopFavouritePagingList<T> {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_page")
    @Expose
    private int totalPage;
    @SerializedName("list")
    @Expose
    private List<T> list = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
