
package com.tokopedia.core.database.recharge.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relationships {

    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("operator")
    @Expose
    private Operator operator;

    /**
     * 
     * @return
     *     The category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * 
     * @param operator
     *     The operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
