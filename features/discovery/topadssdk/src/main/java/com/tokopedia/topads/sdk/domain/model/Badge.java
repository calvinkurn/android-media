package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Badge {

    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_SHOW = "show";

    private String title;
    private String imageUrl;
    private Boolean show;

    public Badge(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Badge(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_TITLE)) {
            setTitle(object.getString(KEY_TITLE));
        }
        if(!object.isNull(KEY_IMAGE_URL)) {
            setImageUrl(object.getString(KEY_IMAGE_URL));
        }
        if(!object.isNull(KEY_SHOW)) {
            setShow(object.getBoolean(KEY_SHOW));
        }
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean isShow() {
        return show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
