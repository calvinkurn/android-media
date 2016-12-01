package com.tokopedia.core.database.recharge.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ricoharisin on 7/14/16.
 */
public class OperatorData {

    @SerializedName("data")
    @Expose
    private List<Operator> data;

    public List<Operator> getData() {
        return data;
    }

    public void setData(List<Operator> data) {
        this.data = data;
    }

}
