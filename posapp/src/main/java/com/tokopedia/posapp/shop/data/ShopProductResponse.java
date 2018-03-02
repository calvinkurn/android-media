package com.tokopedia.posapp.shop.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.posapp.base.data.pojo.Paging;

import java.util.ArrayList;

/**
 * Created by okasurya on 9/4/17.
 */

public class ShopProductResponse {
    @SerializedName("list")
    @Expose
    private java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> list = new ArrayList<List>();

    @SerializedName("paging")
    private Paging paging;

    @SerializedName("total_data")
    private int totalData;

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }
}
