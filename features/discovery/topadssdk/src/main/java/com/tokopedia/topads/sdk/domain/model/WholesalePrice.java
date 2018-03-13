package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class WholesalePrice {

    private static final String KEY_QUANTITY_MIN = "quantity_min_format";
    private static final String KEY_QUANTITY_MAX = "quantity_max_format";
    private static final String KEY_PRICE = "price_format";

    private String quantityMinFormat;
    private String quantityMaxFormat;
    private String priceFormat;

    public WholesalePrice(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_QUANTITY_MIN)){
            setQuantityMinFormat(object.getString(KEY_QUANTITY_MIN));
        }
        if(!object.isNull(KEY_QUANTITY_MAX)){
            setQuantityMaxFormat(object.getString(KEY_QUANTITY_MAX));
        }
        if(!object.isNull(KEY_PRICE)){
            setPriceFormat(object.getString(KEY_PRICE));
        }
    }

    public String getQuantityMinFormat() {
        return quantityMinFormat;
    }

    public void setQuantityMinFormat(String quantityMinFormat) {
        this.quantityMinFormat = quantityMinFormat;
    }

    public String getQuantityMaxFormat() {
        return quantityMaxFormat;
    }

    public void setQuantityMaxFormat(String quantityMaxFormat) {
        this.quantityMaxFormat = quantityMaxFormat;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }
}
