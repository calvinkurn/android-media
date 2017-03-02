package com.tokopedia.digital.cart.model;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class Relation {

    private RelationData data;

    public Relation(RelationData data) {
        this.data = data;
    }

    public RelationData getData() {
        return data;
    }

    public void setData(RelationData data) {
        this.data = data;
    }
}
