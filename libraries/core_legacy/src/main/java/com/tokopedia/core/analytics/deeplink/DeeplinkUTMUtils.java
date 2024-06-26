package com.tokopedia.core.analytics.deeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource;
import com.tokopedia.analyticsdebugger.cassava.Cassava;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Campaign;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Herdi_WORK on 05.06.17.
 */

public class DeeplinkUTMUtils {

    private static final String REFERRER_NAME = "android.intent.extra.REFERRER_NAME";
    private static final String QUICK_SEARCH_BOX = "com.google.android.googlequicksearchbox";
    private static final String APP_CRAWLER = "com.google.appcrawler";
    private static final String KEY_AMP = "amp";
    private static final String TOKOPEDIA_DOMAIN = "tokopedia.com";

    private DeeplinkUTMUtils() {

    }

    public static Uri simplifyUrl(String url) {
        Uri afUri = Uri.parse(url);
        StringBuilder newUri = new StringBuilder(afUri.getQueryParameter("af_dp"));
        Map<String, String> maps = splitQuery(afUri);
        for (Map.Entry<String, String> imap : maps.entrySet()) {
            switch (imap.getKey()) {
                case AppEventTracking.GTM.UTM_SOURCE:
                    newUri.append(AppEventTracking.GTM.UTM_SOURCE_APPEND).append(imap.getValue());
                    break;
                case AppEventTracking.GTM.UTM_MEDIUM:
                    newUri.append(AppEventTracking.GTM.UTM_MEDIUM_APPEND).append(imap.getValue());
                    break;
                case AppEventTracking.GTM.UTM_TERM:
                    newUri.append(AppEventTracking.GTM.UTM_TERM_APPEND).append(imap.getValue());
                    break;
                case AppEventTracking.GTM.UTM_CONTENT:
                    newUri.append(AppEventTracking.GTM.UTM_CONTENT_APPEND).append(imap.getValue());
                    break;
                case AppEventTracking.GTM.UTM_CAMPAIGN:
                    newUri.append(AppEventTracking.GTM.UTM_CAMPAIGN_APPEND).append(imap.getValue());
                    break;
                case AppEventTracking.GTM.UTM_GCLID:
                    newUri.append(AppEventTracking.GTM.UTM_GCLID_APPEND).append(imap.getValue());
                    break;
            }
        }
        return Uri.parse(newUri.toString());
    }


    public static Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = (url != null) ? url.getQuery() : "";
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (Exception e) {
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

    public static Campaign convertUrlCampaign(Activity activity, Uri uri1, boolean isOriginalUrlAmp) {
        Map<String, String> maps = splitQuery(uri1);
        Campaign campaign = new Campaign();

        Uri referrerUri = getReferrer(activity);

        boolean isAmpUri = "true".equalsIgnoreCase(maps.get(KEY_AMP));

        if (isOriginalUrlAmp) {
            campaign = createAmpOrganicCampaign(maps);
        } else if (uri1 != null && isValidCampaignUrl(uri1) && !isAmpUri) {
            //iuf valida campaign and amp is false
            if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
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
            }
        } else {
            // App was referred via a deep link
            if (referrerUri != null && referrerUri.getScheme() != null && (referrerUri.getScheme().equals("http") || referrerUri.getScheme().equals("https"))) {
                // App was opened from a browser
                String host = referrerUri.getHost();

                if (host == null) host = "";

                if (host.contains("tokopedia-com.cdn.ampproject.org")) {
                    campaign = createAmpOrganicCampaign(maps);
                } else if (host.contains("www.google")) {
                    if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                        campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
                    } else {
                        campaign.setUtmSource("google.com");
                        campaign.setUtmMedium("organic");
                        campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");
                        campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                        campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    }
                } else if (host.contains(TOKOPEDIA_DOMAIN)) {
                    //send event if client id is present
                    if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.X_CLID))) {
                        if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                            campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
                        } else {
                            campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                                    maps.get(AppEventTracking.GTM.UTM_SOURCE) : host);
                            campaign.setUtmMedium(maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ?
                                    maps.get(AppEventTracking.GTM.UTM_MEDIUM) : "");
                            campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                                    maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "");
                            campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                                    maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                            campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                                    maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                        }
                        campaign.setExternalClientId(maps.get(AppEventTracking.GTM.X_CLID) != null ?
                                maps.get(AppEventTracking.GTM.X_CLID) : "");
                        campaign.setXAtttribute(maps.get(AppEventTracking.GTM.X_ATTR) != null ?
                                maps.get(AppEventTracking.GTM.X_ATTR) : "");
                    }
                } else {
                    if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                        campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
                    } else {
                        campaign.setUtmSource(maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ?
                                maps.get(AppEventTracking.GTM.UTM_SOURCE) : host);
                        campaign.setUtmMedium(maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_MEDIUM) : "referral");
                        campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "");
                        campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                        campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    }
                    campaign.setExternalClientId(maps.get(AppEventTracking.GTM.X_CLID) != null ?
                            maps.get(AppEventTracking.GTM.X_CLID) : "");
                    campaign.setXAtttribute(maps.get(AppEventTracking.GTM.X_ATTR) != null ?
                            maps.get(AppEventTracking.GTM.X_ATTR) : "");
                }

            } else if (referrerUri != null && referrerUri.getScheme() != null && referrerUri.getScheme().equals("android-app")) {
                // App was opened from another app

                String referrerPackage = referrerUri.getAuthority();
                String tokopediaPackage = "";
                if (activity != null) {
                    tokopediaPackage = activity.getPackageName();
                }

                if (tokopediaPackage.equals(referrerPackage)) {
                    //do nothing
                } else if (QUICK_SEARCH_BOX.equals(referrerPackage)) {
                    // App was opened from the Google app
                    if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                        campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
                    } else {
                        campaign.setUtmSource("google_app");
                        campaign.setUtmMedium("organic");
                        campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");
                        campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                        campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    }
                    campaign.setExternalClientId(maps.get(AppEventTracking.GTM.X_CLID) != null ?
                            maps.get(AppEventTracking.GTM.X_CLID) : "");
                    campaign.setXAtttribute(maps.get(AppEventTracking.GTM.X_ATTR) != null ?
                            maps.get(AppEventTracking.GTM.X_ATTR) : "");
                } else if (!APP_CRAWLER.equals(referrerPackage)) {
                    // App was deep linked into from another app (excl. Google crawler)
                    if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
                        campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
                    } else {
                        campaign.setUtmSource(referrerPackage == null ? "" : referrerPackage);
                        campaign.setUtmMedium(maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_MEDIUM) : "referral");
                        campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");
                        campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                                maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
                        campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                                maps.get(AppEventTracking.GTM.UTM_TERM) : "");
                    }
                    campaign.setExternalClientId(maps.get(AppEventTracking.GTM.X_CLID) != null ?
                            maps.get(AppEventTracking.GTM.X_CLID) : "");
                    campaign.setXAtttribute(maps.get(AppEventTracking.GTM.X_ATTR) != null ?
                            maps.get(AppEventTracking.GTM.X_ATTR) : "");
                }
            }
        }
        if (GlobalConfig.isAllowDebuggingTools() && activity != null) {
            Map<String, Object> map = new HashMap<>(campaign.getCampaign());
            map.put("url", uri1.toString());
            map.put("has_amp_in_original_url", isOriginalUrlAmp);
            if (referrerUri!= null) {
                map.put("referrer", referrerUri.toString());
                map.put("referrer_host", referrerUri.getHost());
            }
            Cassava.save(map, "campaignTrack (not sent, Detail only)", AnalyticsSource.OTHER);
        }
        return campaign;
    }

    private static Campaign createAmpOrganicCampaign(Map<String, String> maps) {
        Campaign campaign = new Campaign();
        if (!TextUtils.isEmpty(maps.get(AppEventTracking.GTM.UTM_GCLID))) {
            campaign.setGclid(maps.get(AppEventTracking.GTM.UTM_GCLID));
        } else {
            campaign.setUtmSource("amp");
            campaign.setUtmMedium("organic");
            campaign.setUtmCampaign(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null ?
                    maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) : "none");
            campaign.setUtmContent(maps.get(AppEventTracking.GTM.UTM_CONTENT) != null ?
                    maps.get(AppEventTracking.GTM.UTM_CONTENT) : "");
            campaign.setUtmTerm(maps.get(AppEventTracking.GTM.UTM_TERM) != null ?
                    maps.get(AppEventTracking.GTM.UTM_TERM) : "");
        }
        campaign.setExternalClientId(maps.get(AppEventTracking.GTM.X_CLID) != null ?
                maps.get(AppEventTracking.GTM.X_CLID) : "");
        campaign.setXAtttribute(maps.get(AppEventTracking.GTM.X_ATTR) != null ?
                maps.get(AppEventTracking.GTM.X_ATTR) : "");
        return campaign;
    }

    /**
     * Returns the referrer who started the Activity.
     */
    public static Uri getReferrer(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return activity.getReferrer();
        }
        return getReferrerCompatible(activity);
    }

    /**
     * Returns the referrer on devices running SDK versions lower than 22.
     */
    private static Uri getReferrerCompatible(Activity activity) {
        Intent intent = activity.getIntent();
        Uri referrerUri = intent.getParcelableExtra(Intent.EXTRA_REFERRER);
        if (referrerUri != null) {
            return referrerUri;
        }
        String referrer = intent.getStringExtra(REFERRER_NAME);
        if (referrer != null) {
            // Try parsing the referrer URL; if it's invalid, return null
            try {
                return Uri.parse(referrer);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
