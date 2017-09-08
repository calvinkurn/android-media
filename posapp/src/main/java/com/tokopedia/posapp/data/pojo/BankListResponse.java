package com.tokopedia.posapp.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankListResponse {
    @SerializedName("list")
    @Expose
    private List<BankItemResponse> list;

    public List<BankItemResponse> getList() {
        return list;
    }

    public void setList(List<BankItemResponse> list) {
        this.list = list;
    }
}
