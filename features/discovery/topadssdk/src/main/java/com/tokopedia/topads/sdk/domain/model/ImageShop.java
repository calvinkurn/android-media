package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class ImageShop {

    private static final String KEY_COVER = "cover";
    private static final String KEY_S_URL = "s_url";
    private static final String KEY_XS_URL = "xs_url";
    private static final String KEY_COVER_ECS = "cover_ecs";
    private static final String KEY_S_ECS = "s_ecs";
    private static final String KEY_XS_ECS = "xs_ecs";

    private String cover;
    private String sUrl;
    private String xsUrl;
    private String coverEcs;
    private String sEcs;
    private String xsEcs;

    public ImageShop(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_COVER)) {
            setCover(object.getString(KEY_COVER));
        }
        if(!object.isNull(KEY_S_URL)) {
            setsUrl(object.getString(KEY_S_URL));
        }
        if(!object.isNull(KEY_XS_URL)) {
            setXsUrl(object.getString(KEY_XS_URL));
        }
        if(!object.isNull(KEY_COVER_ECS)) {
            setCoverEcs(object.getString(KEY_COVER_ECS));
        }
        if(!object.isNull(KEY_S_ECS)) {
            setsEcs(object.getString(KEY_S_ECS));
        }
        if(!object.isNull(KEY_XS_ECS)) {
            setXsEcs(object.getString(KEY_XS_ECS));
        }
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getXsUrl() {
        return xsUrl;
    }

    public void setXsUrl(String xsUrl) {
        this.xsUrl = xsUrl;
    }

    public String getCoverEcs() {
        return coverEcs;
    }

    public void setCoverEcs(String coverEcs) {
        this.coverEcs = coverEcs;
    }

    public String getsEcs() {
        return sEcs;
    }

    public void setsEcs(String sEcs) {
        this.sEcs = sEcs;
    }

    public String getXsEcs() {
        return xsEcs;
    }

    public void setXsEcs(String xsEcs) {
        this.xsEcs = xsEcs;
    }
}
