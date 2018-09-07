package com.tokopedia.digital.widget.data.entity.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class RelationshipEntity {

    @SerializedName("category")
    @Expose
    private CategoryEntity category;
    @SerializedName("operator")
    @Expose
    private OperatorEntity operator;

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public OperatorEntity getOperator() {
        return operator;
    }

    public void setOperator(OperatorEntity operator) {
        this.operator = operator;
    }
}
