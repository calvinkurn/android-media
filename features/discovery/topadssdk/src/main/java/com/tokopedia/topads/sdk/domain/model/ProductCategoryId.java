package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 10/25/17.
 */
public class ProductCategoryId {

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    private int id;
    private String name;

    public ProductCategoryId(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)) {
            setId(object.getInt(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)) {
            setName(object.getString(KEY_NAME));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
