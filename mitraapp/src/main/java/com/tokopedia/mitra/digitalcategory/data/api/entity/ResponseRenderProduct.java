package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
class ResponseRenderProduct {

    @SerializedName("operator")
    @Expose
    private ResponseOperator operator;

    @SerializedName("input_fields")
    @Expose
    private List<ResponseProductInputField> inputFields;

    public ResponseOperator getOperator() {
        return operator;
    }

    public List<ResponseProductInputField> getInputFields() {
        return inputFields;
    }

}
