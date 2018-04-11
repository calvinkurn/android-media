package com.tokopedia.groupchat.common.analytics;

/**
 * @author by nisie on 3/29/18.
 */

public class EEPromotion {

    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_CREATIVE = "creative";
    static final String KEY_POSITION = "position";
    static final String KEY_CREATIVE_URL = "creative_url";
    static final String ATTRIBUTION = "attribution";
    public static final String NAME_GROUPCHAT = "/groupchat";

    String id;
    String name;
    int position;
    String creative;
    String creativeUrl;
    String attribution;

    public EEPromotion(String id, String name, int position, String creative, String creativeUrl,
                       String attribution) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.creative = creative;
        this.creativeUrl = creativeUrl;
        this.attribution = attribution;
    }

    /**
     * banner_id
     */
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreative() {
        return creative;
    }

    public int getPosition() {
        return position;
    }

    public String getCreativeUrl() {
        return creativeUrl;
    }

    public String getAttribution() {
        return attribution;
    }
}
