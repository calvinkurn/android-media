package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Category {

    private static final String KEY_ID = "id";

    private String id;

    public Category(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getString(KEY_ID));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
