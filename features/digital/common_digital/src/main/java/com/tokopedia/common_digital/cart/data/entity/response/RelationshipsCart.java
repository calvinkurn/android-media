package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class RelationshipsCart {

    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("operator")
    @Expose
    private Operator operator;
    @SerializedName("product")
    @Expose
    private Product product;

    public Category getCategory() {
        return category;
    }

    public Operator getOperator() {
        return operator;
    }

    public Product getProduct() {
        return product;
    }
}
