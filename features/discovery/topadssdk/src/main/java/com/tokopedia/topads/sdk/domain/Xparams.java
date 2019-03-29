package com.tokopedia.topads.sdk.domain;

/**
 * Created by errysuprayogi on 7/25/18.
 */
public class Xparams {
    private int product_id;
    private String product_name;
    private int child_cat_id;
    private int source_shop_id;

    public Xparams() {
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getChild_cat_id() {
        return child_cat_id;
    }

    public void setChild_cat_id(int child_cat_id) {
        this.child_cat_id = child_cat_id;
    }

    public int getSource_shop_id() {
        return source_shop_id;
    }

    public void setSource_shop_id(int source_shop_id) {
        this.source_shop_id = source_shop_id;
    }
}
