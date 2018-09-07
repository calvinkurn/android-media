package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmData {

    private static final String KEY_ID = "id";
    private static final String KEY_AD_REF_KEY = "ad_ref_key";
    private static final String KEY_REDIRECT = "redirect";
    private static final String KEY_AD_CLICK_URL = "ad_click_url";
    private static final String KEY_CPM = "headline";
    private static final String KEY_APPLINKS = "applinks";

    private int id;
    private String adRefKey;
    private String redirect;
    private String adClickUrl;
    private Cpm cpm;
    private String applinks;

    public CpmData(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getInt(KEY_ID));
        }
        if(!object.isNull(KEY_AD_REF_KEY)){
            setAdRefKey(object.getString(KEY_AD_REF_KEY));
        }
        if(!object.isNull(KEY_REDIRECT)){
            setRedirect(object.getString(KEY_REDIRECT));
        }
        if(!object.isNull(KEY_AD_CLICK_URL)){
            setAdClickUrl(object.getString(KEY_AD_CLICK_URL));
        }
        if(!object.isNull(KEY_CPM)){
            setCpm(new Cpm(object.getJSONObject(KEY_CPM)));
        }
        if(!object.isNull(KEY_APPLINKS)){
            setApplinks(object.getString(KEY_APPLINKS));
        }
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getAdClickUrl() {
        return adClickUrl;
    }

    public void setAdClickUrl(String adClickUrl) {
        this.adClickUrl = adClickUrl;
    }

    public Cpm getCpm() {
        return cpm;
    }

    public void setCpm(Cpm cpm) {
        this.cpm = cpm;
    }

    public class Cpm {

        private static final String KEY_TEMPLATE_ID = "template_id";
        private static final String KEY_NAME = "name";
        private static final String KEY_CPM_IMAGE = "image";
        private static final String KEY_BADGES = "badges";
        private static final String KEY_PROMOTED_TEXT = "promoted_text";
        private static final String KEY_DESCRIPTION = "description";
        private static final String KEY_URI = "uri";
        private static final String KEY_SHOP = "shop";
        private static final String KEY_CTA_TEXT = "button_text";

        private int templateId;
        private String name = "";
        private CpmImage cpmImage;
        private List<Badge> badges = new ArrayList<>();
        private String promotedText = "";
        private String uri = "";
        private String decription = "";
        private CpmShop cpmShop;
        private String cta = "";

        public Cpm(JSONObject object) throws JSONException {
            if(!object.isNull(KEY_TEMPLATE_ID)){
                setTemplateId(object.getInt(KEY_TEMPLATE_ID));
            }
            if(!object.isNull(KEY_NAME)){
                setName(object.getString(KEY_NAME));
            }
            if(!object.isNull(KEY_CPM_IMAGE)){
                setCpmImage(new CpmImage(object.getJSONObject(KEY_CPM_IMAGE)));
            }
            if(!object.isNull(KEY_BADGES)) {
                JSONArray badgeArray = object.getJSONArray(KEY_BADGES);
                for (int i = 0; i < badgeArray.length(); i++) {
                    badges.add(new Badge(badgeArray.getJSONObject(i)));
                }
            }
            if(!object.isNull(KEY_PROMOTED_TEXT)){
                setPromotedText(object.getString(KEY_PROMOTED_TEXT));
            }
            if(!object.isNull(KEY_URI)){
                setUri(object.getString(KEY_URI));
            }
            if(!object.isNull(KEY_DESCRIPTION)){
                setDecription(object.getString(KEY_DESCRIPTION));
            }
            if(!object.isNull(KEY_SHOP)){
                setCpmShop(new CpmShop(object.getJSONObject(KEY_SHOP)));
            }
            if(!object.isNull(KEY_CTA_TEXT)) {
                setCta(object.getString(KEY_CTA_TEXT));
            }
        }

        public String getCta() {
            return cta;
        }

        public void setCta(String cta) {
            this.cta = cta;
        }

        public String getDecription() {
            return decription;
        }

        public void setDecription(String decription) {
            this.decription = decription;
        }

        public CpmShop getCpmShop() {
            return cpmShop;
        }

        public void setCpmShop(CpmShop cpmShop) {
            this.cpmShop = cpmShop;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CpmImage getCpmImage() {
            return cpmImage;
        }

        public void setCpmImage(CpmImage cpmImage) {
            this.cpmImage = cpmImage;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

        public String getPromotedText() {
            return promotedText;
        }

        public void setPromotedText(String promotedText) {
            this.promotedText = promotedText;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

}
