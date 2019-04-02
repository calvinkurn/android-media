
package com.tokopedia.abstraction.common.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PagingList<T> {

    @SerializedName("paging")
    @Expose
    private PagingDetail paging;
    @SerializedName("total_data")
    @Expose
    private long totalData;
    @SerializedName("list")
    @Expose
    private List<T> list = new ArrayList<>();

    public PagingDetail getPaging() {
        return paging;
    }

    public void setPaging(PagingDetail paging) {
        this.paging = paging;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
