package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 17/09/18.
 */
public class LePreapproveModel {

    @SerializedName("fieldData")
    @Expose
    private FieldDataModel fieldData = new FieldDataModel();

    public FieldDataModel getFieldData() {
        return fieldData;
    }
}
