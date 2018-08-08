package com.tokopedia.digital.widget.data.entity.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.widget.data.entity.product.DataEntity;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class OperatorEntity {

    @SerializedName("data")
    @Expose
    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }
}
