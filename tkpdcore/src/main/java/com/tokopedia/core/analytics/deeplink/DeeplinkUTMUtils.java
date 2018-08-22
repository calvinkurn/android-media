package com.tokopedia.core.analytics.deeplink;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.appindexing.AndroidAppUri;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Campaign;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Herdi_WORK on 05.06.17.
 */

public class DeeplinkUTMUtils {

    private DeeplinkUTMUtils() {

    }

    public static Uri simplifyUrl(String url) {
        Uri afUri = Uri.parse(url);
        String newUri = afUri.getQueryParameter("af_dp");
        Map<String, String> maps = splitQuery(afUri);
        for (Map.Entry<String, String> imap : maps.entrySet()) {
            switch (imap.getKey()) {
                case AppEventTracking.GTM.UTM_SOURCE:
                    newUri += AppEventTracking.GTM.UTM_SOURCE_APPEND + imap.getValue();
                    break;
                case AppEventTracking.GTM.UTM_MEDIUM:
                    newUri += AppEventTracking.GTM.UTM_MEDIUM_APPEND + imap.getValue();
                    break;
                case AppEventTracking.GTM.UTM_TERM:
                    newUri += AppEventTracking.GTM.UTM_TERM_APPEND + imap.getValue();
                    break;
                case AppEventTracking.GTM.UTM_CONTENT:
                    newUri += AppEventTracking.GTM.UTM_CONTENT_APPEND + imap.getValue();
                    break;
                case AppEventTracking.GTM.UTM_CAMPAIGN:
                    newUri += AppEventTracking.GTM.UTM_CAMPAIGN_APPEND + imap.getValue();
                    break;
                case AppEventTracking.GTM.UTM_GCLID:
                    newUri += AppEventTracking.GTM.UTM_GCLID_APPEND + imap.getValue();
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
        return maps.containsKey(AppEventTracking.GTM.UTM_GCLID) ||
                (maps.containsKey(AppEventTracking.GTM.UTM_SOURCE) &&
                        maps.containsKey(AppEventTracking.GTM.UTM_MEDIUM) &&
                        maps.containsKey(AppEventTracking.GTM.UTM_CAMPAIGN));
    }

    private static final String QUICK_SEARCH_BOX = "com.google.android.googlequicksearchbox";
    private static final String APP_CRAWLER = "com.google.appcrawler";

    public static Campaign convertUrlCampaign(Uri uri) {
        Map<String, String> maps = splitQuery(uri);
        Campaign campaign = new Campaign();


        if (uri == null) {
            // App was opened directly by the user
        } else {
            // App was referred via a deep link
            if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                // App was opened from a browser
                String host = uri.getHost();
                if (host.equals("www.google.com")) {

                    campaign.setUtmSource("google.com");
                    campaign.setUtmMedium("organic");
                    campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");

                    campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                    campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                            maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                            maps.get(AppEventTracking.GTM.UTM_GCLID) : "");

                } else {

                    campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                            maps.get(AppEventTracking.GTM.UTM_SOURCE) : "");
                    campaign.setUtmMedium("referral");
                    campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");

                    campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                    campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                            maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                            maps.get(AppEventTracking.GTM.UTM_GCLID) : "");

                }

            } else if (uri.getScheme().equals("android-app")) {
                // App was opened from another app
                AndroidAppUri appUri = AndroidAppUri.newAndroidAppUri(uri);
                String referrerPackage = appUri.getPackageName();
                if (QUICK_SEARCH_BOX.equals(referrerPackage)) {
                    // App was opened from the Google app
                    campaign.setUtmSource("google_app");
                    campaign.setUtmMedium("organic");
                    campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");

                    campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                    campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                            maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                            maps.get(AppEventTracking.GTM.UTM_GCLID) : "");

                } else if (!APP_CRAWLER.equals(referrerPackage)) {
                    // App was deep linked into from another app (excl. Google crawler)
//
                    campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                            maps.get(AppEventTracking.GTM.UTM_SOURCE) : "");
                    campaign.setUtmMedium("referral");
                    campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");


                    campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                            maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                    campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                            maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                            maps.get(AppEventTracking.GTM.UTM_GCLID) : "");

                }

            } else {
                campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                        maps.get(AppEventTracking.GTM.UTM_SOURCE) : "");
                campaign.setUtmMedium(maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ?
                        maps.get(AppEventTracking.GTM.UTM_MEDIUM) : "");
                campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                        maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "");
                campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                        maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                        maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID) != null ?
                        maps.get(AppEventTracking.GTM.UTM_GCLID) : "");
            }

        }


        return campaign;
    }

}
