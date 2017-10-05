package com.tokopedia.posapp.data.pojo.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/19/17.
 */
// TODO: 9/20/17 clean up this mess too
public class ListResponse<T> {
    @SerializedName("list")
    @Expose
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
