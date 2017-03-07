package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public void setCategory(Category category) {
        this.category = category;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
