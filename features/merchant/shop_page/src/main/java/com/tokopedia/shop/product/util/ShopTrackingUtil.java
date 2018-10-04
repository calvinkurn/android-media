package com.tokopedia.shop.product.util;

import android.text.TextUtils;

/**
 * Created by nakama on 05/04/18.
 */

public class ShopTrackingUtil {

    public static String appendTrackerAttributionIfNeeded(String applink, String trackingAttribution) {
        if (TextUtils.isEmpty(trackingAttribution)) {
            return applink;
        }

        try {
            trackingAttribution = java.net.URLEncoder.encode(trackingAttribution, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            trackingAttribution = trackingAttribution.replaceAll(trackingAttribution, "%20");
        }

        if (applink.contains("?")) {
            return applink + "&tracker_attribution=" + trackingAttribution;
        } else {
            return applink + "?tracker_attribution=" + trackingAttribution;
        }
    }
}
