package com.tokopedia.digital.cart.model;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class Relationships {
    private Relation relationProduct;
    private Relation relationCategory;
    private Relation relationOperator;

    public Relation getRelationProduct() {
        return relationProduct;
    }

    public void setRelationProduct(Relation relationProduct) {
        this.relationProduct = relationProduct;
    }

    public Relation getRelationCategory() {
        return relationCategory;
    }

    public void setRelationCategory(Relation relationCategory) {
        this.relationCategory = relationCategory;
    }

    public Relation getRelationOperator() {
        return relationOperator;
    }

    public void setRelationOperator(Relation relationOperator) {
        this.relationOperator = relationOperator;
    }
}
