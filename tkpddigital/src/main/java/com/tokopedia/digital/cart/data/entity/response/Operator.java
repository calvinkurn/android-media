package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class Operator {

    @SerializedName("data")
    @Expose
    private RelationData data;

    public void setData(RelationData data) {
        this.data = data;
    }

    public RelationData getData() {
        return data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
