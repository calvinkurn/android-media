package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/15/17.
 */
// TODO: 9/15/17 move it to another package
class ListResponse<T> {
    @SerializedName("list")
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
