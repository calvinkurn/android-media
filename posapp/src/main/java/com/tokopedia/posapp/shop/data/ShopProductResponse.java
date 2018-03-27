package com.tokopedia.posapp.shop.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.base.data.pojo.Paging;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 9/4/17.
 */

public class ShopProductResponse {
    @SerializedName("list")
    @Expose
    private List<ProductDetail> list = new ArrayList<>();

    @SerializedName("paging")
    private Paging paging;

    @SerializedName("total_data")
    private int totalData;

    public List<ProductDetail> getList() {
        return list;
    }

    public void setList(List<ProductDetail> list) {
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
