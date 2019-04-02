package com.tokopedia.core.analytics;

import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Campaign;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by okasurya on 8/4/17.
 */

public class CampaignUtil {
    public static Campaign getCampaignFromQuery(String query) {
        Campaign campaign = new Campaign();
        Map<String, String> maps = getMappedQuery(query);

        campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "");

        campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");

        campaign.setUtmMedium(maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ?
                maps.get(AppEventTracking.GTM.UTM_MEDIUM) : "");

        campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                maps.get(AppEventTracking.GTM.UTM_SOURCE) : "");

        campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                maps.get(AppEventTracking.GTM.UTM_TERM) : "");

        campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                maps.get(AppEventTracking.GTM.UTM_GCLID) : "");

        return campaign;
    }

    private static Map<String, String> getMappedQuery(String query) {
        Map<String, String> maps = new LinkedHashMap<>();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        maps.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return maps;
    }
}
