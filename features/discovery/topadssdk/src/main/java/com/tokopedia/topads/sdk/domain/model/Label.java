package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Label {

    public static final String KEY_TITLE = "title";
    public static final String KEY_COLOR = "color";

    private String title;
    private String color;

    public Label(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_TITLE)) {
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_COLOR)){
            setColor(object.getString(KEY_COLOR));
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
