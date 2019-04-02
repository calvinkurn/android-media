package com.tokopedia.core.analytics.nishikino.model;

import com.tokopedia.core.analytics.AppEventTracking;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by alvarisi on 6/8/2016.
 */
public class Campaign {
    private Map<String, Object> campaignMap = new HashMap<>();

    public Campaign() {

    }

    public Campaign(Campaign campaign){
        this.campaignMap.putAll(campaign.getCampaign());
    }

    public void setUtmSource(String utmSource) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_SOURCE, utmSource);
    }

    public void setUtmMedium(String utmMedium) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_MEDIUM, utmMedium);
    }

    public void setUtmCampaign(String utmCampaign) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_CAMPAIGN, utmCampaign);
    }

    public void setUtmContent(String utmContent) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_CONTENT, utmContent);
    }

    public void setUtmTerm(String utmTerm) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_TERM, utmTerm);
    }

    public void setGclid(String gclid) {
        this.campaignMap.put(AppEventTracking.GTM.UTM_GCLID, gclid);
    }

    public void setScreenName(String screenName) {
        this.campaignMap.put("screenName", screenName);
    }

    public Map<String, Object> getCampaign() {
        return this.campaignMap;
    }

    public Map<String, Object> getNullCampaignMap() {
        setUtmCampaign(null);
        setUtmContent(null);
        setUtmMedium(null);
        setUtmSource(null);
        setUtmTerm(null);
        setGclid(null);
        setScreenName(null);
        return campaignMap;
    }
}
