package com.tokopedia.tkpd.selling.orderReject.model;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 6/3/2016.
 */
@Parcel
public class ModelEditPrice {
    public static final String MODEL_EDIT_PRICE_KEY = "model_edit_price_key";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_PRICE_CURRENCY = "product_price_currency";
    public static final String PRODUCT_WEIGHT_VALUE = "product_weight_value";
    public static final String PRODUCT_WEIGHT_UNIT = "product_weight_unit";

    String product_id;
    String product_price;
    String product_price_currency;
    String product_weight;
    String product_weight_value;
    String user_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_price_currency() {
        return product_price_currency;
    }

    public void setProduct_price_currency(String product_price_currency) {
        this.product_price_currency = product_price_currency;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }


    public String getProduct_weight_value() {
        return product_weight_value;
    }

    public void setProduct_weight_value(String product_weight_value) {
        this.product_weight_value = product_weight_value;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
