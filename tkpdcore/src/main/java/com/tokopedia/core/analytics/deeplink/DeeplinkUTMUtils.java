package com.tokopedia.core.analytics.deeplink;

import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.Campaign;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Herdi_WORK on 05.06.17.
 */

public class DeeplinkUTMUtils {

    private DeeplinkUTMUtils(){

    }

    public static Uri simplifyUrl(String url) {
        Uri afUri = Uri.parse(url);
        String newUri = afUri.getQueryParameter("af_dp");
        Map<String, String> maps = splitQuery(afUri);
        for (Map.Entry<String, String> imap : maps.entrySet()) {
            switch (imap.getKey()) {
                case "utm_source":
                    newUri += "&utm_source=" + imap.getValue();
                    break;
                case "utm_medium":
                    newUri += "&utm_medium=" + imap.getValue();
                    break;
                case "utm_term":
                    newUri += "&utm_term=" + imap.getValue();
                    break;
                case "utm_content":
                    newUri += "&utm_content=" + imap.getValue();
                    break;
                case "utm_campaign":
                    newUri += "&utm_campaign=" + imap.getValue();
                    break;
                case "gclid":
                    newUri += "&gclid=" + imap.getValue();
                    break;
            }
        }
        return Uri.parse(newUri);
    }


    public static Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }


    public static boolean isValidCampaignUrl(Uri uri) {
        Map<String, String> maps = splitQuery(uri);
        return maps.containsKey("utm_source") &&
                maps.containsKey("utm_medium") &&
                maps.containsKey("utm_campaign");
    }

    public static Campaign convertUrlCampaign(Uri uri) {
        Map<String, String> maps = splitQuery(uri);
        Campaign campaign = new Campaign();
        campaign.setUtmSource(maps.get("utm_source") != null ?
                maps.get("utm_source") : "");
        campaign.setUtmMedium(maps.get("utm_medium") != null ?
                maps.get("utm_medium") : "");
        campaign.setUtmCampaign(maps.get("utm_campaign") != null ?
                maps.get("utm_campaign") : "");
        campaign.setUtmContent(maps.get("utm_content") != null ?
                maps.get("utm_content") : "");
        campaign.setUtmTerm(maps.get("utm_term") != null ?
                maps.get("utm_term") : "");
        campaign.setGclid(maps.get("gclid") != null ?
                maps.get("gclid") : "");
        return campaign;
    }

}
