package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseRenderOperator {

    @SerializedName("input_fields")
    @Expose
    private List<ResponseInputField> inputFields;

    @SerializedName("render_product")
    @Expose
    private List<ResponseRenderProduct> renderProduct;

    public List<ResponseInputField> getInputFields() {
        return inputFields;
    }

    public List<ResponseRenderProduct> getRenderProduct() {
        return renderProduct;
    }

}
