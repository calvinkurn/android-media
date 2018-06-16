package com.tokopedia.reksadana.view.data.initdata;

import android.support.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.reksadana.view.data.common.Result;

import java.util.List;

public class Register {
    @Expose
    @SerializedName("fieldData")
    private List<FieldData> fieldData;

    @Expose
    @SerializedName("result")
    private Result result;

    public Register(List<FieldData> fieldData, Result result) {
        this.fieldData = fieldData;
        this.result = result;
    }

    public List<FieldData> fieldData() {
        return fieldData;
    }

    public Result result() {
        return result;
    }
}
