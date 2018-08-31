package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseRenderOperator {

    @SerializedName("input_fields")
    @Expose
    private List<ResponseOperatorInputField> inputFields;

    @SerializedName("render_product")
    @Expose
    private List<ResponseRenderProduct> renderProduct;

    public List<ResponseOperatorInputField> getInputFields() {
        return inputFields;
    }

    public List<ResponseRenderProduct> getRenderProduct() {
        return renderProduct;
    }

}
